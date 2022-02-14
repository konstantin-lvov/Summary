package ru.kl.summary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONObject;
import ru.kl.summary.services.BackendUrlResolver;
import ru.kl.summary.services.GetHandler;
import ru.kl.summary.services.TokenHandler;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private TextView organizationName;
    private TextView password;
    private Button login;
    private View snackView;


    private final String INTERNAL_DIR_PROPERTIES_FILE_NAME = "runtime.properties";

    private final String TOKEN_PROPERTIES_NAME = "auth.token";
    private final String URL_LOGIN_ENDPOINT = "/mobileLogin";
    private final String URL_TOKEN_LOGIN_ENDPOINT = "/mobileTokenLogin";
    private final String URL_PARAMS_TOKEN = "token=";
    private final String URL_PARAMS_ORG = "organization=";
    private final String URL_PARAMS_PAS = "password=";

    private String TOKEN;
    private String BACKEND_SITE;

    private String tmpNextLIne;

    boolean tokenExist = false;
    boolean addressOfSiteExist = false;
    String assetsPropFileContext;
    String internalDirPropFileContext;

    GetHandler getHandler = null;

    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final File propertiesFile = new File(getBaseContext().getFilesDir(), INTERNAL_DIR_PROPERTIES_FILE_NAME);

        BACKEND_SITE = BackendUrlResolver.getBackendUrl();

        TokenHandler tokenHandler = new TokenHandler();
        try {
            TOKEN = tokenHandler.getToken();
            tokenExist = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tokenExist) {
            System.out.println("token exist, making test request" + TOKEN);
            try {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                String url = BACKEND_SITE + URL_TOKEN_LOGIN_ENDPOINT
                        + "?" + URL_PARAMS_TOKEN + TOKEN;
                getHandler = new GetHandler(url, countDownLatch);
                getHandler.makeRequest();
                if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
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
                if (organizationName.getText().toString().toCharArray().length > 1
                        && password.getText().toString().toCharArray().length > 1) {
                    String url = BACKEND_SITE + URL_LOGIN_ENDPOINT
                            + "?" + URL_PARAMS_ORG + organizationName.getText().toString()
                            + "&" + URL_PARAMS_PAS + password.getText().toString();
                    try {
                        CountDownLatch countDownLatch = new CountDownLatch(1);
                        getHandler = new GetHandler(url, countDownLatch);
                        getHandler.makeRequest();
                        if (!countDownLatch.await(5, TimeUnit.SECONDS)) {
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
                }
            }
        });
    }

    public void logIn(View view) {


        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}