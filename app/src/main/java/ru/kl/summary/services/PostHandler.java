package ru.kl.summary.services;

import okhttp3.*;

import java.io.IOException;
import java.io.InterruptedIOException;

import static java.lang.Thread.sleep;

public class PostHandler {
    Thread t;
    OkHttpClient client = new OkHttpClient();
    Response response = null;

    public PostHandler (){}

    public Response makeRequest(String url, RequestBody formBody){


        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        t = new Thread(){
            public void run (){
                try {
                    response = client.newCall(request).execute();
                    while (t != null){
                        t.sleep(100);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        };
        t.start();
        return response;
    }

    public void stop(){
        t = null;
    }

}
