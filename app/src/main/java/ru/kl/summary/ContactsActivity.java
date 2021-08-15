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

import java.util.Date;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        TextView textView = findViewById(R.id.ContactsTextView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        Button backButton = findViewById(R.id.ContactsBackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToMainActivity(view);
            }
        });

        RequestHandler requestHandler = new RequestHandler();
        JSONArray jsonArray = requestHandler.GETRequest("/mobileContacts");

        System.out.println(jsonArray.toString());
        System.out.println("json array length " + jsonArray.length());

        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tmpJSONObject = jsonArray.getJSONObject(i);
                stringBuilder.append(i+1 + ": " + tmpJSONObject.getString("contact") + "\n");
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
