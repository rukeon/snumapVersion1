package com.snumap.snumapversion1;

import  android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.snumap.snumapversion1.API.StatusCheckApi;
import com.snumap.snumapversion1.Model.StatusModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class IntroActivity extends AppCompatActivity {

    private StatusCheckApi buildingApi;
    private boolean todayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if(!isNetworkStat(IntroActivity.this))
        {
            Thread splashTimer = new Thread() {
                public void run() {
                    try {
                        sleep(1500);
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
        } else {
            statusCheck("2015-08-19");
        }
    }

    private void statusCheck(String date) {
        final long Time;
        Time = System.currentTimeMillis();

        StatusService statusService = new StatusService();
        Status dayInfo = new Status(date);

        statusService.getStatusCheckApi().getStatusInfo(dayInfo, new Callback<StatusModel>() {
            @Override
            public void success(final StatusModel statusModel, Response response) {

                Thread splashTimer = new Thread() {
                    public void run() {
                        try {
                            todayStatus = statusModel.getStatus();
                            Log.e("todayStatus", String.valueOf(todayStatus));

                            if (!todayStatus) {
                                long checkTime = System.currentTimeMillis();
                                while (checkTime <= Time + 1500)
                                {
                                    checkTime = System.currentTimeMillis();
                                }
                                startActivity(new Intent(IntroActivity.this,
                                        MainActivity.class));
                            } else {
                                Intent goToError = new Intent(IntroActivity.this, ErrorActivity.class);
                                startActivity(goToError);
                                Log.e("todayStatus", String.valueOf(todayStatus));
                            }
                        } catch (Exception e) {
                            Log.e("ex", e.toString());
                        } finally {
                            finish();
                        }
                    }
                };
                splashTimer.start();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public static boolean isNetworkStat( Context context ) {
        ConnectivityManager manager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean blte_4g = false;
        if(lte_4g != null)
            blte_4g = lte_4g.isConnected();
        if( mobile != null ) {
            if (mobile.isConnected() || wifi.isConnected() || blte_4g)
                return true;
        } else {
            if ( wifi.isConnected() || blte_4g )
                return true;
        }
        return false;
    }
}
