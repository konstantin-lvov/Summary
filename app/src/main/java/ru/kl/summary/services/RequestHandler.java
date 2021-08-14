package ru.kl.summary.services;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.kl.summary.MyApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestHandler {

    private final String ASSETS_PROPERTIES_FILE_NAME = "app.properties";
    private final String BACKEND_SITE_PROPERTIES_NAME = "backend.site";

    String backendSite;
    String assetsPropFileContext;

    boolean addressOfSiteExist = false;

    public RequestHandler(){
        try {
            InputStream is = MyApp.getContext().getAssets().open(ASSETS_PROPERTIES_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            assetsPropFileContext = new String(buffer);

            Scanner scanner = new Scanner(assetsPropFileContext);
            String tmpNextLIne;
            while (scanner.hasNextLine()) {
                tmpNextLIne = scanner.nextLine();
                if (tmpNextLIne.contains(BACKEND_SITE_PROPERTIES_NAME)) {
                    backendSite = tmpNextLIne.split("=")[1];
                    addressOfSiteExist = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray GETRequest(String urlEndpoint){
        CountDownLatch countDownLatch = new CountDownLatch(1);
        GetHandler getHandler = null;
        JSONArray jsonArray = null;
        TokenHandler tokenHandler = new TokenHandler();

        try {
            String token = tokenHandler.getToken();

            String url = backendSite + urlEndpoint
                    + "?" + "token=" + token;
            getHandler = new GetHandler(url, countDownLatch);
            getHandler.makeRequest();
            if(!countDownLatch.await(5, TimeUnit.SECONDS)){
                throw new TimeoutException();
            }
            if (getHandler.isGotResponse()) {
                String clearJson = getHandler.getResponseFromServer();
                jsonArray = new JSONArray(clearJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
