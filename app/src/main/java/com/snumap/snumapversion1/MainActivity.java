package com.snumap.snumapversion1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String URL = "file:///android_asset/leaflet/index.html";

    //현재위치 관련
    ImageView currentPostion;
    GPSTracker gps;

    // 카카오톡 관련
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private final String imgSrcLink = "http://snumap.com/snumap/images/share.png"; // 전송시 딸려가는 이미지다.

    // SharedPreference 처리
    List<User> users;
    ListUserPref complexObject;
    ComplexPreferences complexPreferences;

    // 드르워 레이아웃 처리
    DrawerLayout drawerLayout;
    View drawerView;
    ListView drawerList;
    Button menu;

    // 뒤로 가기 버튼 컨트롤
    private BackPressCloseHandler backPressCloseHandler;

    // 슬라이딩 메뉴 아랫부분 클릭 시 close되게
    FrameLayout close;

    private String[] menu_name = {
            "건물검색", "길찾기", "즐겨찾기", "설정"};
    private  Integer image_id[] = {R.drawable.ic_search,
            R.drawable.ic_directions_walk,
            R.drawable.ic_archive,
            R.drawable.ic_settings };

    // 메뉴 버튼 클릭 시 어댑터
    Customlistadapter adapter;

    private InputMethodManager imm; // for keybaord control
    private WebView mapView;

    // db 처리하는 부분
    public MyDatabase db;

    CustomAutoCompleteView myAutoComplete;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;

    // just to add some initial value
    ArrayList<String> item = new ArrayList<String>();

    // 초성 검색을 위한 arrayList
    ArrayList<String> searchList = new ArrayList<String>();

    ArrayList<DataSet> search_result = new ArrayList<DataSet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 현재위치 관련
        currentPostion = (ImageView) findViewById(R.id.currentPostion);

        // 카카오톡 인스턴스 생성
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            e.getMessage();
        }

        // sharedpreference 인스턴스 생성
        complexPreferences = ComplexPreferences.getComplexPreferences(this, "mypref", MODE_PRIVATE);

        if (complexObject == null)
        {
            users = new ArrayList<User>();
            complexObject = new ListUserPref();
        } else {
            complexObject = complexPreferences.getObject("list", ListUserPref.class);
        }

        // 뒤로가기 버튼 클릭 관련
        backPressCloseHandler = new BackPressCloseHandler(this);

        // 키보드 관리를 위한 시작, 아래 함수 있다.
        init();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        menu = (Button) findViewById(R.id.btn_menu);
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (menu.isActivated())
                {
                    drawerLayout.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                } else {
                    drawerLayout.openDrawer(drawerView);
                }
                menu.setActivated(!menu.isActivated());
            }
        });

        drawerLayout.setDrawerListener(myDrawerListener);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        close = (FrameLayout) findViewById(R.id.frameForClose);

        drawerView.setOnTouchListener(new View.OnTouchListener() {
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

        drawerList = (ListView)findViewById(R.id.drawerlist);
        drawerList.setAdapter(adapter);

        // 건물검색, 길찾기, 즐겨찾기, 설정의 항목들 클릭 시 발생 이벤트 처리
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                SlidingListData data = adapter.getmSlidingListData().get(position);
                String activity = data.getmActivity().toString();

                switch (activity) {
                    case "건물검색":
                        drawerLayout.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자

                        // 아래의 4개 메뉴바 제거
                        LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                        list_menu.setVisibility(View.GONE);

                        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoComplete);
                        myAutoComplete.setVisibility(View.VISIBLE);
                        myAutoComplete.setText("");

                        mapView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                // TODO Auto-generated method stub
                                super.onPageFinished(view, url);
                                view.loadUrl("javascript:removeMarker()();");
                            }
                        });
                        //load webpage from assets
                        mapView.loadUrl(URL);
                        break;

                    case "길찾기":
                        drawerLayout.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToSearchRouteIntent =
                                new Intent(MainActivity.this, SearchRouteActivity.class);
                        startActivity(goToSearchRouteIntent);
                        break;

                    case "즐겨찾기":
                        drawerLayout.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자

                        Intent newIntent =
                                new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(newIntent);
                        break;

                    case "설정":
                        drawerLayout.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자

                        Intent settingIntent =
                                new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                }
            }
        });

        // 드로워 레이아웃 옆 색깔 투명으로 만들기
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        close.setOnClickListener(this);

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);
        addList();//검색리스트를 애드 해준다

        // autocompletetextview is in activity_main.xml
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoComplete);

        // add the listener so it will tries to suggest while the user types
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        // set our adapter
        myAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_row, item);
        myAutoComplete.setAdapter(myAdapter); //....여기까지(디비)

        mapView = (WebView) findViewById(R.id.map_view);
        mapView.getSettings().setJavaScriptEnabled(true);

        mapView.loadUrl(URL);

        // 자동검색 바 클릭시 검색어 없애기
        myAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAutoComplete.setText("");
                showKeyboard();
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.GONE);
            }
        });

        // 자동 검색 리스트에서 검색어 클릭 시 해당 위치로 이동
        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String query = myAdapter.getItem(position).toString();
                DataSet result = db.selectDatabyId(query);
                result.setPositionChecker(100); // 디폴트 값 부여
//                String name = result.getName();
//                String number = result.getNumber();
                final String latitude = result.getLatitude();
                final String longitude = result.getLongitude();
                hideKeyboard();

                search_result.add(0, result);

                mapView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // TODO Auto-generated method stub
                        super.onPageFinished(view, url);

                        view.loadUrl("javascript:moveLeaflet([" + latitude + "," + longitude + "])();");
                    }
                });
                //load webpage from assets
                mapView.loadUrl(URL);

                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.VISIBLE);
            }
        });

        currentPostion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPostion.isActivated())
                {
                    mapView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            // TODO Auto-generated method stub
                            super.onPageFinished(view, url);
                            view.loadUrl("javascript:removeMarker()();");
                        }
                    });
                    //load webpage from assets
                    mapView.loadUrl(URL);
                } else {
                    gps = new GPSTracker(MainActivity.this);

                    if (gps.canGetLocation()) {
                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();

                        mapView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                // TODO Auto-generated method stub
                                super.onPageFinished(view, url);

                                view.loadUrl("javascript:moveLeaflet([" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "])();");
                            }
                        });
                        //load webpage from assets
                        mapView.loadUrl(URL);
                    } else {
                        gps.showSettingsAlert();
                    }
                }
                currentPostion.setActivated(!currentPostion.isActivated());
            }
        });

        // 즐겨찾기에서 클릭 해서 넘어왔을 경우의 activity 처리
        Intent favIntent = getIntent();
        Bundle b = favIntent.getExtras();

        if(b != null)
        {
            String favQuery =(String) b.get("favorite");

            DataSet result = db.selectDatabyId(favQuery);
            result.setPositionChecker(100); // 디폴트 값 부여
            final String latitude = result.getLatitude();
            final String longitude = result.getLongitude();

            search_result.add(0, result);

            mapView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:moveLeaflet([" + latitude + "," + longitude + "])();");
                }
            });
            //load webpage from assets
            mapView.loadUrl(URL);

            LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
            list_menu.setVisibility(View.VISIBLE);
        }

        // 검색 완료 후 아래 나타나는 4가지 항목에 관한 처리 시작
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String favorite_name = search_result.get(0).get_id();
                        User tmpUser = new User(favorite_name);

                        if (complexObject == null)
                        {
                            users = new ArrayList<User>();
                            complexObject = new ListUserPref();
                        } else {
                            complexObject = complexPreferences.getObject("list", ListUserPref.class);
                            if ((complexObject == null)) {
                                users = new ArrayList<User>();
                                complexObject = new ListUserPref();
                            } else {
                                users = complexObject.users;
                            }
                        }

                        if ((complexObject.getUsers() == null)) {
                            users.add(tmpUser);
                            complexObject.setUsers(users);
                        } else if (complexObject.isExist(tmpUser)) {
                            Toast.makeText(MainActivity.this, "이미 존재합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            users.add(tmpUser);
                            complexObject.setUsers(users);
                        }
                        complexPreferences.putObject("list", complexObject);
                        complexPreferences.commit();

                        Toast.makeText(MainActivity.this,
                                "등록되었습니다. 메뉴->즐겨찾기에서 확인가능합니다",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                        list_menu.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        // 즐겨찾기 버튼 클릭 이벤트 처리
        LinearLayout favorite = (LinearLayout) findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아래의 4개 메뉴바 제거
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.GONE);

                String favorite_name = search_result.get(0).get_id();
                new AlertDialog.Builder(MainActivity.this).setMessage( favorite_name + "을 즐겨찾는 건물에 등록하시겠습니까?").setPositiveButton("확인", dialogClickListener).setNegativeButton("취소", dialogClickListener).show();
            }
        });

        // 출발지 클릭 이벤트 처리ㅡ
        LinearLayout start = (LinearLayout) findViewById(R.id.start_position);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아래의 4개 메뉴바 제거
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.GONE);

                myAutoComplete.setText("");

                String start_name = search_result.get(0).get_id();
                Intent goToSearchRoute = new Intent(MainActivity.this, SearchRouteActivity.class);
                goToSearchRoute.putExtra("FROM", start_name);
                startActivity(goToSearchRoute);
//                // 텍스트가 7글자 이상일 경우 ..붙이기
//                if (start_name.length() > 7)
//                {
//                    start_name = start_name.substring(0,6) + "..";
//                }
            }
        });

        // 도착지 클릭 이벤트 처리
        final LinearLayout end = (LinearLayout) findViewById(R.id.finish_position);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아래의 4개 메뉴바 제거
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.GONE);

                myAutoComplete.setText("");

                String end_name = search_result.get(0).get_id();
                Intent goToSearchRoute = new Intent(MainActivity.this, SearchRouteActivity.class);
                goToSearchRoute.putExtra("TO", end_name);
                startActivity(goToSearchRoute);
            }
        });

        // 공유하기 클릭 이벤트 처리...
        LinearLayout link = (LinearLayout) findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아래의 4개 메뉴바 제거
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                list_menu.setVisibility(View.GONE);

                // 카카오톡 전송을 위한 정보 가져오기
                String user_search = search_result.get(0).get_id();
                String buildingIndex = search_result.get(0).getName();

                sendLink(imgSrcLink, buildingIndex, user_search);
            }
        }); // ... 여기까지(4가지 이벤트 처리)
    }

    // 카카오톡 전송관련
    private void sendLink(String imgSrcLink, String index, String search){
        try {
            kakaoTalkLinkMessageBuilder.addText(search);
            kakaoTalkLinkMessageBuilder.addImage(imgSrcLink, 269, 95);
            kakaoTalkLinkMessageBuilder.addWebButton("웹으로 이동", "http://snumap.com/cmap/selectize/examples/dynamic.html?idx=" + index);
            final String linkContents = kakaoTalkLinkMessageBuilder.build();
            kakaoLink.sendMessage(linkContents, this);
        } catch (KakaoParameterException e) {
            e.getMessage();
        }
    }

    private void addList() {
        searchList = db.insertBuildingName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameForClose:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
        }
    }

    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener(){

        @Override
        public void onDrawerClosed(View drawerView) {
//            ImageView clear = (ImageView) findViewById(R.id.imgClear);
//            clear.setVisibility(View.GONE);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
//            ImageView clear = (ImageView) findViewById(R.id.imgClear);
//            clear.setVisibility(View.VISIBLE);
//            clear.bringToFront();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
//            textPrompt.setText("onDrawerSlide: " + String.format("%.2f", slideOffset));
        }

        @Override
        public void onDrawerStateChanged(int newState) {
//            String state;
//            switch(newState){
//                case DrawerLayout.STATE_IDLE:
//                    state = "STATE_IDLE";
//                    break;
//                case DrawerLayout.STATE_DRAGGING:
//                    state = "STATE_DRAGGING";
//                    break;
//                case DrawerLayout.STATE_SETTLING:
//                    state = "STATE_SETTLING";
//                    break;
//                default:
//                    state = "unknown!";
//            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void init(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoComplete);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showKeyboard(){
        imm.showSoftInput(myAutoComplete, 0);
    }

    // 뒤로가기 버튼 컨트롤
    @Override
    public void onBackPressed() {
        LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
        if (list_menu.getVisibility() == View.VISIBLE)
        {
            list_menu.setVisibility(View.GONE);
        } else {
            //super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }
    }
}
