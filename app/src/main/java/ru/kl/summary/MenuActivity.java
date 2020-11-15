package ru.kl.summary;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    private TextView message;
    private int counter = 0;
    Button onOff;
    Button settings;
    Button contacts;
    Button sentSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView topLabel = findViewById(R.id.topLabel);
        ImageView background = findViewById(R.id.background);
        onOff = findViewById(R.id.onOff);
        settings = findViewById(R.id.settings);
        contacts = findViewById(R.id.contacts);
        sentSms = findViewById(R.id.sentSms);
    }

    public void goToSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
