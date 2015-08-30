package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.snumap.snumapversion1.Model.RouteModel;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowRouteActivity extends AppCompatActivity {
    private static final String URL = "file:///android_asset/leaflet/index.html";
    WebView mapView;

    Intent mIntent;
    String from;
    String to;

    TextView fromtxt;
    TextView totxt;

    // db 처리하는 부분
    public MyDatabase db;

    private RouteService routeService;
    private int[] latitude;
    private int[] longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
//
        mapView = (WebView) findViewById(R.id.map_viewSR);
        mapView.getSettings().setJavaScriptEnabled(true);

//        mapView.loadUrl(URL);

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);

        fromtxt = (TextView) findViewById(R.id.from);
        totxt = (TextView) findViewById(R.id.to);

        mIntent = getIntent();
        Bundle b = mIntent.getExtras();

        if(b != null) {
            from = mIntent.getStringExtra("FROM");
            to = mIntent.getStringExtra("TO");
        }

        DataSet resultFrom = db.selectDatabyId(from);
        DataSet resultTo = db.selectDatabyId(to);

        // 추후에 number에 idx를 넣자
        String idxFrom = resultFrom.getNumber();
        String idxTo = resultTo.getNumber();

        getData(idxFrom, idxTo);
    }

    private void getData(String from, String to){
        RouteService routeService = new RouteService();

        routeService.getRouteApi().getRoute(from, to, new Callback<RouteModel>() {
            @Override
            public void success(final RouteModel routeModel, Response response) {
                Thread routeReciever = new Thread() {
                    public void run() {
                        try {
                            latitude = routeModel.getLatitude();
                            longitude = routeModel.getLongitude();

                            mapView.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    // TODO Auto-generated method stub
                                    super.onPageFinished(view, url);

                                    String lat = Arrays.toString(latitude);
                                    String longitu = Arrays.toString(longitude);

                                    Log.e("도대체 뭐가 들어가는 게냐?" , "drawRoute(\"\" + lat + \"\\\",\\\"\" + longitu +  \"\")();");

                                    view.loadUrl("javascript:drawRoute(\"" + lat + "\",\"" + longitu +  "\")();");
                                }
                            });
                            //load webpage from assets
                            mapView.loadUrl(URL);
                        } catch (Exception e) {
                        Log.e("exxxxxx", e.toString());
                    } finally {

                        }
                }
            };
            routeReciever.start();
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e("여긴가?", error.toString());
            }
        });
    }
}
