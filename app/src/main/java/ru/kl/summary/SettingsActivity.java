package ru.kl.summary;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {
    private Button setup;
    private Button templates;
    private Button keywords;
    private Button endlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setup = findViewById(R.id.setup_b);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSetup(view);
            }
        });

        templates = findViewById(R.id.templates_b);
        templates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTemplates(view);
            }
        });

        keywords = findViewById(R.id.keywords_b);
        keywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToKeywords(view);
            }
        });

        endlines = findViewById(R.id.endline_b);
        endlines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEndlines(view);
            }
        });
    }

    public void goToSetup(View view){
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }

    public void goToTemplates(View view){
        Intent intent = new Intent(this, TemplatesActivity.class);
        startActivity(intent);
    }

    public void goToKeywords(View view){
        Intent intent = new Intent(this, KeywordsActivity.class);
        startActivity(intent);
    }

    public void goToEndlines(View view){
        Intent intent = new Intent(this, EndlinesActivity.class);
        startActivity(intent);
    }
}
