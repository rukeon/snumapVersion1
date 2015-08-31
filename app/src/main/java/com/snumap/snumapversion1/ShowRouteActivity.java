package com.snumap.snumapversion1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView prospectTime;

    // db 처리하는 부분
    public MyDatabase db;

    private RouteService routeService;
    private int[] latitude;
    private int[] longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        mapView = (WebView) findViewById(R.id.map_viewSR);
        mapView.getSettings().setJavaScriptEnabled(true);

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);

        fromtxt = (TextView) findViewById(R.id.fromSR);
        totxt = (TextView) findViewById(R.id.toSR);
        prospectTime = (TextView) findViewById(R.id.prospectTime);

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

        String forFromtxt = resultFrom.get_id();
        String forTotxt = resultTo.get_id();
//        String formatForFrom = "";
//        String formatForTo = "";
//
//        if(forFromtxt.length() >6)
//        {
//            formatForFrom = forFromtxt.substring(0,6) + "...";
//        } else {
//            formatForFrom = forFromtxt;
//        }
//
//        if(forTotxt.length() >6)
//        {
//            formatForTo = forTotxt.substring(0,6) + "...";
//        } else {
//            formatForTo = forTotxt;
//        }

        fromtxt.setText(forFromtxt);
        totxt.setText(forTotxt);

        getData(idxFrom, idxTo);
    }

    private void getData(String from, String to){
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        RouteService routeService = new RouteService();

        routeService.getRouteApi().getRoute(from, to, new Callback<RouteModel>() {
            @Override
            public void success(final RouteModel routeModel, Response response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Thread routeReciever = new Thread() {
                    public void run() {
                        try {
                            latitude = routeModel.getLatitude();
                            longitude = routeModel.getLongitude();

                            mapView.post(new Runnable() {
                                @Override
                                public void run() {
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
                                }
                            });

//                            mapView.setWebViewClient(new WebViewClient() {
//                                @Override
//                                public void onPageFinished(WebView view, String url) {
//                                    // TODO Auto-generated method stub
//                                    super.onPageFinished(view, url);
//
//                                    String lat = Arrays.toString(latitude);
//                                    String longitu = Arrays.toString(longitude);
//
//                                    Log.e("도대체 뭐가 들어가는 게냐?" , "drawRoute(\"\" + lat + \"\\\",\\\"\" + longitu +  \"\")();");
//
//                                    view.loadUrl("javascript:drawRoute(\"" + lat + "\",\"" + longitu +  "\")();");
//                                }
//                            });
//                            //load webpage from assets
//                            mapView.loadUrl(URL);
                        } catch (Exception e) {
                        Log.e("exxxxxx", e.toString());
                    } finally {
                            Thread splashTimer = new Thread() {
                                public void run() {
                                    try {
                                        int distance = 0;
                                        for (int i = 0; i < latitude.length-1; ++i)
                                        {
                                            int nodeDistance = (int) Math.sqrt((latitude[i] - latitude[i+1])*(latitude[i] - latitude[i+1]) + (longitude[i] - longitude[i+1])*(longitude[i] - longitude[i+1]));
                                            distance += nodeDistance;
                                        }

                                        Log.e("distance가 어케 되는거냐?", String.valueOf(distance));

                                        int time = (int) Math.round((distance*0.6)/1000*15);
                                        if (time >= 5)
                                        {
                                            ++time;
                                        }
                                        if (time == 0)
                                        {
                                            time = 1;
                                            if (distance == 0)
                                                time = 0;
                                        }

                                        String text = "예상소요 시간: <font color='red'>" + String.valueOf(time) + "분</font>";
                                        prospectTime.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
                                    } catch (Exception e) {
                                        Log.e("ex", e.toString());
                                    } finally {

                                    }
                                }
                            };
                            splashTimer.start();
                        }
                }
            };
            routeReciever.start();
            }
            @Override
            public void failure(RetrofitError error)
            {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 뒤로가기 버튼 컨트롤
    @Override
    public void onBackPressed() {
        Intent intent_go_back = new Intent(ShowRouteActivity.this, SearchRouteActivity.class);
        intent_go_back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_go_back);
    }
}
