package ru.kl.summary;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.kl.summary.services.RequestHandler;
import ru.kl.summary.services.TokenHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class CallsInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_info);
        TextView textView = findViewById(R.id.CallsInfoTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Button backButton = findViewById(R.id.CallsInfoBackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToMainActivity(view);
            }
        });

        RequestHandler requestHandler = new RequestHandler();
        JSONArray jsonArray = requestHandler.GETRequest("/mobileCallsInfo");

        System.out.println(jsonArray.toString());
        System.out.println("json array length " + jsonArray.length());

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tmpJSONObject = jsonArray.getJSONObject(i);
                Date date = new Date(Long.valueOf(tmpJSONObject.getString("date")));
                stringBuilder.append("Дата разговора: " + date.toString() + "\n");
                stringBuilder.append("Номер телефона: " + tmpJSONObject.getString("phoneNumber") + "\n");
                stringBuilder.append("Текст СМС: " + tmpJSONObject.getString("parsedSms") + "\n\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        textView.setText(stringBuilder.toString());
    }

    public void goToMainActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
