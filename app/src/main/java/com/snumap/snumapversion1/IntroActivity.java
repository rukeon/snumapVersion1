package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Thread splashTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    // Advance to the next screen.
                    startActivity(new Intent(IntroActivity.this,
                            MainActivity.class));//}
                } catch (Exception e) {
                    Log.e("ex", e.toString());
                } finally {
                    finish();
                }
            }
        };
        splashTimer.start();
    }
}
