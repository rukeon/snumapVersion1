package com.snumap.snumapversion1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


// NOTE!! 실제로 FAQ이다.
public class AskActivity extends AppCompatActivity {
    ImageView goBackFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        goBackFaq = (ImageView) findViewById(R.id.goBackFaq);

        goBackFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
