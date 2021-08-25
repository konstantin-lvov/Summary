package ru.kl.summary.services;

import java.io.*;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import ru.kl.summary.MyApp;

public class TokenHandler extends AppCompatActivity{

    private final String ASSETS_PROPERTIES_FILE_NAME = "app.properties";
    private final String INTERNAL_DIR_PROPERTIES_FILE_NAME = "runtime.properties";
    private final String TOKEN_PROPERTIES_NAME = "auth.token";
    String internalDirPropFileContext;
    private String TOKEN;
    private String tmpNextLIne;
    boolean tokenExist = false;

    public String getToken() throws IOException {


        Context context = MyApp.getContext();
        InputStream is = context.getAssets().open(ASSETS_PROPERTIES_FILE_NAME);

        final File propertiesFile = new File(MyApp.getContext().getFilesDir(), INTERNAL_DIR_PROPERTIES_FILE_NAME);

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

        try{
            File file = new File(internalDirPropFileContext);
            System.out.println("TOKENHANDLER INTERNAL DIR FILE EXIST - " + file.exists());
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

        return TOKEN;
    }
}
