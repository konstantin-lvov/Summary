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
    Button callsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView topLabel = findViewById(R.id.topLabel);
        ImageView background = findViewById(R.id.background);
        onOff = findViewById(R.id.onOff);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings(view);
            }
        });

        contacts = findViewById(R.id.contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToContacts(view);
            }
        });

        callsInfo = findViewById(R.id.callsInfo);
        callsInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToCallsInfo(view);
            }
        });
    }

    public void goToSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToContacts(View view){
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);
    }

    public void goToCallsInfo(View view){
        Intent intent = new Intent(this, CallsInfoActivity.class);
        startActivity(intent);
    }
}
