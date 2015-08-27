package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    // 드르워 레이아웃 처리
    DrawerLayout drawerLayoutSet;
    View drawerViewSet;
    ListView drawerListSet;
    Button SetMenuBtn;

    // 슬라이딩 메뉴 아랫부분 클릭 시 close되게
    FrameLayout closeSet;

    private String[] menu_name = {
            "건물검색", "길찾기", "즐겨찾기", "설정"};
    private  Integer image_id[] = {R.drawable.ic_search,
            R.drawable.ic_directions_walk,
            R.drawable.ic_archive,
            R.drawable.ic_settings };

    // 메뉴 버튼 클릭 시 어댑터
    Customlistadapter adapter;

    Button howToUseBtn;
    Button faQbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Drawerlayout 부분
        drawerLayoutSet = (DrawerLayout)findViewById(R.id.drawer_layoutSet);
        drawerViewSet = (View)findViewById(R.id.drawerSet);

        SetMenuBtn = (Button) findViewById(R.id.SetMenuBtn);
        SetMenuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (SetMenuBtn.isActivated())
                {
                    drawerLayoutSet.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                } else {
                    drawerLayoutSet.openDrawer(drawerViewSet);
                }
                SetMenuBtn.setActivated(!SetMenuBtn.isActivated());
            }
        });

        drawerLayoutSet.setDrawerListener(myDrawerListener);
        drawerLayoutSet.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        closeSet = (FrameLayout) findViewById(R.id.frameForCloseSet);

        drawerViewSet.setOnTouchListener(new View.OnTouchListener() {
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

        drawerListSet = (ListView)findViewById(R.id.drawerlistSet);
        drawerListSet.setAdapter(adapter);

        // 건물검색, 길찾기, 즐겨찾기, 설정의 항목들 클릭 시 발생 이벤트 처리
        drawerListSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                SlidingListData data = adapter.getmSlidingListData().get(position);
                String activity = data.getmActivity().toString();

                switch (activity) {
                    case "건물검색":
                        Intent goToMainIntent =
                                new Intent(SettingActivity.this, MainActivity.class);
                        goToMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToMainIntent);
                        break;

                    case "즐겨찾기":
                        Intent goToFavIntent =
                                new Intent(SettingActivity.this, FavoriteActivity.class);
                        startActivity(goToFavIntent);
                        break;

                    case "설정":
                        drawerLayoutSet.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        break;
                }
            }
        });

        // 드로워 레이아웃 옆 색깔 투명으로 만들기
        drawerLayoutSet.setScrimColor(getResources().getColor(android.R.color.transparent));
        closeSet.setOnClickListener(this);

        // 본 내용 시작 부분
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameForCloseSet:
                drawerLayoutSet.closeDrawer(Gravity.RIGHT);
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
