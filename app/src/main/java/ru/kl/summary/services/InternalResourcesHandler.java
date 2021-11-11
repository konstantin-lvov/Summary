package ru.kl.summary.services;

import android.content.Context;
import ru.kl.summary.MyApp;

import java.io.IOException;
import java.io.InputStream;

public class InternalResourcesHandler {

    private final String ASSETS_PROPERTIES_FILE_NAME = "app.properties";

    public InternalResourcesHandler (){}

    public String getAssetsPropFileName (){
        return ASSETS_PROPERTIES_FILE_NAME;
    }

    public String getAssetsPropFileContext (){
        String assetsPropFileContext = "";
        try {
            InputStream is = MyApp.getContext().getAssets().open(ASSETS_PROPERTIES_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            assetsPropFileContext = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetsPropFileContext;
    }
}
