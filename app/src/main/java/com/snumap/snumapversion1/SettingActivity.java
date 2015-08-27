package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    Button howToUseBtn;
    Button faQbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        howToUseBtn = (Button) findViewById(R.id.howToUseBtn);
        faQbtn = (Button) findViewById(R.id.faQbtn);

        howToUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Howintent = new Intent(SettingActivity.this, HowToUseActivity.class);
                startActivity(Howintent);
            }
        });

        faQbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FAQintent = new Intent(SettingActivity.this, FaQActivity.class);
                startActivity(FAQintent);
            }
        });
    }
}
