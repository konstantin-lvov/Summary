package ru.kl.summary;


import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ru.kl.summary.services.AudioRecordHandler;
import ru.kl.summary.services.UploadObject;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    private TextView message;
    private int counter = 0;
    TextView switchTextView;
    Switch onOfSwitch;
    Button contacts;
    Button callsInfo;
    TextView infoTextView;

    AudioRecordHandler audioRecordHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        audioRecordHandler = new AudioRecordHandler();
        audioRecordHandler.setSpeakerOn();

        ImageView topLabel = findViewById(R.id.topLabel);
        ImageView background = findViewById(R.id.background);

        infoTextView = findViewById(R.id.infoTextView);

        switchTextView = findViewById(R.id.switchTextView);
        switchTextView.setGravity(Gravity.CENTER);
        switchTextView.setTextColor(Color.RED);

        onOfSwitch = findViewById(R.id.OnOfSwitch);
        onOfSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    onOffRecording(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public void onOffRecording(View view) throws IOException {
        if(this.onOfSwitch.isChecked()){
            System.out.println(this.onOfSwitch.isActivated());
            this.switchTextView.setTextColor(Color.GREEN);
            this.switchTextView.setText("ON");

            audioRecordHandler.startRecording();
        } else {
            System.out.println(this.onOfSwitch.isActivated());
            this.switchTextView.setTextColor(Color.RED);
            this.switchTextView.setText("OFF");

            audioRecordHandler.stopRecording();
            infoTextView.setText(audioRecordHandler.getFilename());

            String nameOfUploadedObject = "record-" + audioRecordHandler.getCurrentAudioFileName();
            UploadObject uploadObject = new UploadObject();
            uploadObject.uploadObject("silver-aurora-294418", "summary-storage",
                    nameOfUploadedObject, audioRecordHandler.getCurrentAudioFilePath());
            uploadObject.stopThread();
        }
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
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
