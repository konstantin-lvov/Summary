package ru.kl.summary.services;

import android.view.accessibility.AccessibilityNodeInfo;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GetHandler {

    private String url = null;
    private CountDownLatch countDownLatch = null;
    private String responseFromServer = null;
    private boolean gotResponse = false;

    public String getResponseFromServer() {
        return responseFromServer;
    }

    public boolean isGotResponse() {
        return gotResponse;
    }

    public GetHandler(String url, CountDownLatch countDownLatch){
        this.url = url;
        this.countDownLatch = countDownLatch;
    }

    public void makeRequest() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                responseFromServer = response.body().string();
                gotResponse = true;
                countDownLatch.countDown();
            }
        });
    }
}
