package ru.kl.summary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import ru.kl.summary.services.GetHandler;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private TextView organizationName;
    private TextView password;
    private Button login;


    private final String ASSETS_PROPERTIES_FILE_NAME = "app.properties";
    private final String INTERNAL_DIR_PROPERTIES_FILE_NAME = "runtime.properties";
    private final String BACKEND_SITE_PROPERTIES_NAME = "backend.site";
    private final String TOKEN_PROPERTIES_NAME = "auth.token";
    private final String URL_LOGIN_ENDPOINT = "/mobileLogin";
    private final String URL_TOKEN_LOGIN_ENDPOINT = "/mobileTokenLogin";
    private final String URL_PARAMS_TOKEN = "token=";
    private final String URL_PARAMS_ORG = "organization=";
    private final String URL_PARAMS_PAS = "password=";


    private String BACKEND_SITE;
    private String TOKEN;

    private String tmpNextLIne;

    boolean tokenExist = false;
    boolean addressOfSiteExist = false;
    String assetsPropFileContext;
    String internalDirPropFileContext;

    GetHandler getHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /*
        1. Читаем из проп файла из внутр каталога, если нет то из ассет каталога
        2. Если в нем есть адрес сайта то все хорошо идем дальше
        3. Если в нем есть токен то выставляем переменные (стр и бул)
            3.1 Пытаемся сделать запрос с текущим токеном
        4. Если токена нет, то забираем у пользователя логин и пароль и идем на сайт с ними
            4.1 Если запрос удачен получаем токен и записываем его во внутреннее хранилище телефона
         */
        /*
        БЛОК КОДА
        Читаем из проп файла из ассет каталога
         */
        final File propertiesFile = new File(getBaseContext().getFilesDir(), INTERNAL_DIR_PROPERTIES_FILE_NAME);
        Context context = getApplicationContext();
        //
        try {
            InputStream is = context.getAssets().open(ASSETS_PROPERTIES_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            assetsPropFileContext = new String(buffer);
            System.out.println(assetsPropFileContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (propertiesFile.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(propertiesFile));
                StringBuilder text = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                bufferedReader.close();
                internalDirPropFileContext = text.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*
        БЛОК КОДА
        Если в проп файле есть адрес сайта то все хорошо идем дальше
        Если в нем есть токен то выставляем переменные (стр и бул)
         */
        try{
            Scanner scanner = new Scanner(assetsPropFileContext);
            while (scanner.hasNextLine()) {
                tmpNextLIne = scanner.nextLine();
                if (tmpNextLIne.contains(BACKEND_SITE_PROPERTIES_NAME)) {
                    BACKEND_SITE = tmpNextLIne.split("=")[1];
                    addressOfSiteExist = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            Scanner scanner = new Scanner(internalDirPropFileContext);
            while (scanner.hasNextLine()) {
                tmpNextLIne = scanner.nextLine();
                if (tmpNextLIne.contains(TOKEN_PROPERTIES_NAME)) {
                    TOKEN = tmpNextLIne.split("=")[1];
                    tokenExist = true;
                }

            }
        } catch (Exception e){
            e.printStackTrace();
        }


        if (!addressOfSiteExist) {
            //надо сделать страницу с вводом желаемого сервера
            System.out.println("Address of site does not exist. Please enter address.");
        }

        /*
        БЛОК КОДА
        Пытаемся сделать запрос с текущим токеном
         */
        if (tokenExist) {
            System.out.println("token exist, making test request");
            try {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                String url = BACKEND_SITE + URL_TOKEN_LOGIN_ENDPOINT
                        + "?" + URL_PARAMS_TOKEN + TOKEN;
                getHandler = new GetHandler(url, countDownLatch);
                getHandler.makeRequest();
                if(!countDownLatch.await(5, TimeUnit.SECONDS)){
                    throw new TimeoutException();
                }
                if (getHandler.isGotResponse()) {
                    try {
                        if (getHandler.getResponseFromServer().equals("OK")) {
                            logIn(new View(this.getBaseContext()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView topLabel = findViewById(R.id.topLabel);
        ImageView background = findViewById(R.id.background);
        organizationName = findViewById(R.id.organizationName);
        password = findViewById(R.id.password_logo);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                БЛОК КОДА
                забираем у пользователя логин и пароль и идем на сайт с ними
                Если запрос удачен получаем токен и записываем его во внутреннее хранилище телефона
                */
                if (organizationName.getText().toString().toCharArray().length > 1
                        && password.getText().toString().toCharArray().length > 1) {
                    String url = BACKEND_SITE + URL_LOGIN_ENDPOINT
                            + "?" + URL_PARAMS_ORG + organizationName.getText().toString()
                            + "&" + URL_PARAMS_PAS + password.getText().toString();
                    try {
                        CountDownLatch countDownLatch = new CountDownLatch(1);
                        getHandler = new GetHandler(url, countDownLatch);
                        getHandler.makeRequest();
                        if(!countDownLatch.await(5, TimeUnit.SECONDS)){
                            throw new TimeoutException();
                        }
                        if (getHandler.isGotResponse()) {
                            try {
                                JSONObject jsonObject = new JSONObject(getHandler.getResponseFromServer());
                                TOKEN = jsonObject.getString("token");
                                FileWriter writer = new FileWriter(propertiesFile);
                                writer.write(TOKEN_PROPERTIES_NAME + "=" + TOKEN + "\n");
                                writer.flush();
                                writer.close();
                                logIn(view);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // тут будет отображение сообщения пользователю
                }
            }
        });
    }



    public void logIn(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

//    public void makeMultipleRequest(){
//
//        String [] endpoints = {
//                "/mobileCallsInfo",
//                "/mobileContacts",
//                "/mobileEndlineTemplates",
//                "/mobileKeywords",
//                "/mobileSettings",
//                "/mobileSmsTemplates"
//        };
//
//        for (int i = 0; i < endpoints.length; i++){
//            try {
//                String url = BACKEND_SITE + endpoints[i]
//                        + "?" + URL_PARAMS_TOKEN + TOKEN;
//                CountDownLatch countDownLatch = new CountDownLatch(1);
//                makeRequest(url, countDownLatch);
//                if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
//                    throw new TimeoutException();
//                }
//                if (gotResponse) {
//                    try {
//                        System.out.println(responseFromServer);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }
}