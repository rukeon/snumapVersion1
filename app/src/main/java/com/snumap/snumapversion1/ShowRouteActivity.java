package com.snumap.snumapversion1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snumap.snumapversion1.Model.RouteModel;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowRouteActivity extends AppCompatActivity implements View.OnClickListener {
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

    // 드르워 레이아웃 처리
    DrawerLayout drawerLayoutFR;
    View drawerViewFR;
    ListView drawerListFR;
    Button FRMenuBtn;

    // 슬라이딩 메뉴 아랫부분 클릭 시 close되게
    FrameLayout closeFR;

    private String[] menu_name = {
            "건물검색", "길찾기", "즐겨찾기", "설정"};
    private  Integer image_id[] = {R.drawable.ic_search,
            R.drawable.ic_directions_walk_menu,
            R.drawable.ic_archive,
            R.drawable.ic_settings };

    // 메뉴 버튼 클릭 시 어댑터
    Customlistadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        // Drawerlayout 부분
        drawerLayoutFR = (DrawerLayout)findViewById(R.id.drawer_layoutFR);
        drawerViewFR = (View)findViewById(R.id.drawerFR);

        FRMenuBtn = (Button) findViewById(R.id.btn_menuFR);
        FRMenuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (FRMenuBtn.isActivated())
                {
                    drawerLayoutFR.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                } else {
                    drawerLayoutFR.openDrawer(drawerViewFR);
                }
                FRMenuBtn.setActivated(!FRMenuBtn.isActivated());
            }
        });

        drawerLayoutFR.setDrawerListener(myDrawerListener);
        drawerLayoutFR.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        closeFR = (FrameLayout) findViewById(R.id.frameForCloseFR);

        drawerViewFR.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });

        adapter = new Customlistadapter(this);

        for(int i = 0; i < menu_name.length; ++i)
        {
            adapter.addItem(image_id[i], menu_name[i]);
        }

        drawerListFR = (ListView)findViewById(R.id.drawerlistFR);
        drawerListFR.setAdapter(adapter);

        // 건물검색, 길찾기, 즐겨찾기, 설정의 항목들 클릭 시 발생 이벤트 처리
        drawerListFR.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                SlidingListData data = adapter.getmSlidingListData().get(position);
                String activity = data.getmActivity().toString();

                switch (activity) {
                    case "건물검색":
                        drawerLayoutFR.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToMainIntent =
                                new Intent(ShowRouteActivity.this, MainActivity.class);
                        goToMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToMainIntent);
                        break;

                    case "길찾기":
                        drawerLayoutFR.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToSearchRoute =
                                new Intent(ShowRouteActivity.this, SearchRouteActivity.class);
                        startActivity(goToSearchRoute);
                        break;

                    case "즐겨찾기":
                        drawerLayoutFR.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToFavorite =
                                new Intent(ShowRouteActivity.this, FavoriteActivity.class);
                        startActivity(goToFavorite);
                        break;

                    case "설정":
                        drawerLayoutFR.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToSettingIntent =
                                new Intent(ShowRouteActivity.this, SettingActivity.class);
                        startActivity(goToSettingIntent);
                        break;
                }
            }
        });

        // 드로워 레이아웃 옆 색깔 투명으로 만들기
        drawerLayoutFR.setScrimColor(getResources().getColor(android.R.color.transparent));
        closeFR.setOnClickListener(this);

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
                        } catch (Exception e) {
                            Toast.makeText(ShowRouteActivity.this, "에러가 발생했습니다. 알려주시면 조속히 해결하도록 하겠습니다.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameForCloseFR:
                drawerLayoutFR.closeDrawer(Gravity.RIGHT);
                break;
        }
    }

    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener(){
        @Override
        public void onDrawerClosed(View drawerView) {
        }
        @Override
        public void onDrawerOpened(View drawerView) {
        }
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }
        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
}
