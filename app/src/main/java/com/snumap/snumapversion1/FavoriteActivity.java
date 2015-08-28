package com.snumap.snumapversion1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements View.OnClickListener {
    ComplexPreferences complexPreferences;
    ListUserPref complexObject;

    ArrayList<MyItemForListView> arItem;

    TextView deleteTxt;
    TextView cancelTxt;
    TextView deleteTxtTogo;

    MyListAdapter myAdapterForDelete;

    Button orderByDate;
    Button orderByName;

    // 드르워 레이아웃 처리
    DrawerLayout drawerLayoutFav;
    View drawerViewFav;
    ListView drawerListFav;
    Button favMenuBtn;

    // 슬라이딩 메뉴 아랫부분 클릭 시 close되게
    FrameLayout closeFav;

    private String[] menu_name = {
            "건물검색", "길찾기", "즐겨찾기", "설정"};
    private  Integer image_id[] = {R.drawable.ic_search,
            R.drawable.ic_directions_walk,
            R.drawable.ic_archive,
            R.drawable.ic_settings };

    // 메뉴 버튼 클릭 시 어댑터
    Customlistadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Drawerlayout 부분
        drawerLayoutFav = (DrawerLayout)findViewById(R.id.drawer_layoutFav);
        drawerViewFav = (View)findViewById(R.id.drawerFav);

        favMenuBtn = (Button) findViewById(R.id.favMenuBtn);
        favMenuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (favMenuBtn.isActivated())
                {
                    drawerLayoutFav.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                } else {
                    drawerLayoutFav.openDrawer(drawerViewFav);
                }
                favMenuBtn.setActivated(!favMenuBtn.isActivated());
            }
        });

        drawerLayoutFav.setDrawerListener(myDrawerListener);
        drawerLayoutFav.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        closeFav = (FrameLayout) findViewById(R.id.frameForCloseFav);

        drawerViewFav.setOnTouchListener(new View.OnTouchListener() {
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

        drawerListFav = (ListView)findViewById(R.id.drawerlistFav);
        drawerListFav.setAdapter(adapter);

        // 건물검색, 길찾기, 즐겨찾기, 설정의 항목들 클릭 시 발생 이벤트 처리
        drawerListFav.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                SlidingListData data = adapter.getmSlidingListData().get(position);
                String activity = data.getmActivity().toString();

                switch (activity) {
                    case "건물검색":
                        drawerLayoutFav.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToMainIntent =
                                new Intent(FavoriteActivity.this, MainActivity.class);
                        goToMainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(goToMainIntent);
                        break;

                    case "즐겨찾기":
                        drawerLayoutFav.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        break;

                    case "설정":
                        drawerLayoutFav.closeDrawer(Gravity.RIGHT); // 먼저 슬라이딩 메뉴부터 지우자
                        Intent goToSettingIntent =
                                new Intent(FavoriteActivity.this, SettingActivity.class);
                        startActivity(goToSettingIntent);
                        break;
                }
            }
        });

        // 드로워 레이아웃 옆 색깔 투명으로 만들기
        drawerLayoutFav.setScrimColor(getResources().getColor(android.R.color.transparent));
        closeFav.setOnClickListener(this);


        // 본 내용의 list 부분 시작...
        arItem = new ArrayList<MyItemForListView>();

        complexPreferences = ComplexPreferences.getComplexPreferences(this, "mypref", MODE_PRIVATE);
        complexObject = complexPreferences.getObject("list", ListUserPref.class);

        deleteTxt = (TextView) findViewById(R.id.deleteTxt);
        cancelTxt = (TextView) findViewById(R.id.cancelTxt);
        deleteTxtTogo = (TextView) findViewById(R.id.deleteTxtTogo);

        orderByDate = (Button) findViewById(R.id.orderByDate);
        orderByName = (Button) findViewById(R.id.orderByName);

        if (complexObject != null) {
            // 시간순 정렬을 위한....
            List<User> sortedList;
            sortedList = complexObject.users;
            if (sortedList == null)
                return;
            Collections.sort(sortedList, new CustomComparator(true)); // 시간순...

            complexObject.setUsers(sortedList);
            complexPreferences.putObject("list", complexObject);
            complexPreferences.commit();
        }

        // 리스트 뷰에 넣을 LIST 생성
        MyItemForListView mi;
        if (complexObject != null)
        {
            for(User item: complexObject.users){
                mi = new MyItemForListView(R.drawable.pin, item, R.drawable.arrow);
                arItem.add(mi);
            }
        }

        final MyListAdapter myAdapter = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext, arItem);

        final ListView myList;
        myList = (ListView) findViewById(R.id.list);
        myList.setEmptyView(findViewById(R.id.txtForNothingExist));
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout list_menu = (LinearLayout) findViewById(R.id.list_menu);
                if (list_menu.getVisibility() == View.VISIBLE)
                {
                    // do nothing
                } else {
                    Intent goToMainintent = new Intent(FavoriteActivity.this, MainActivity.class);
                    goToMainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    String str = arItem.get(position).name.getName();
                    goToMainintent.putExtra("favorite", str);
                    startActivity(goToMainintent);
                }
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                final int posForDialog = pos;

                new AlertDialog.Builder(FavoriteActivity.this)
                        .setTitle("삭제")
                        .setMessage("정말 지우시겠어요?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (posForDialog != ListView.INVALID_POSITION) {
                                    String str = arItem.get(posForDialog).name.getName();
                                    complexPreferences.delete(complexObject, new User(str));
                                    complexPreferences.commit();

                                    arItem.remove(posForDialog);
                                    myList.clearChoices();
                                    myAdapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });

        deleteTxtTogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (arItem.size() == 0) {

                } else {
                    LinearLayout listLayout2 = (LinearLayout) findViewById(R.id.list_menu2);
                    listLayout2.setVisibility(View.GONE);

                    LinearLayout listLayout = (LinearLayout) findViewById(R.id.list_menu);
                    listLayout.setVisibility(View.VISIBLE);

                    myAdapterForDelete = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext2, arItem);

                    final ListView myList;
                    myList = (ListView) findViewById(R.id.list);
                    myList.setAdapter(myAdapterForDelete);

                    myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int pos, long id) {
                            return true;
                        }
                    });
                }
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                deleteTxt.setText("삭제");

                LinearLayout listLayout2 = (LinearLayout) findViewById(R.id.list_menu2);
                listLayout2.setVisibility(View.VISIBLE);

                LinearLayout listLayout = (LinearLayout) findViewById(R.id.list_menu);
                listLayout.setVisibility(View.GONE);

                final MyListAdapter myAdapter = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext, arItem);

                final ListView myList;
                myList = (ListView) findViewById(R.id.list);
                myList.setAdapter(myAdapter);

                myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        final int posForDialog = pos;

                        new AlertDialog.Builder(FavoriteActivity.this)
                                .setTitle("삭제")
                                .setMessage("정말 지우시겠어요?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (posForDialog != ListView.INVALID_POSITION) {
                                            String str = arItem.get(posForDialog).name.getName();
                                            complexPreferences.delete(complexObject, new User(str));
                                            complexPreferences.commit();

                                            arItem.remove(posForDialog);
                                            myList.clearChoices();
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                        return true;
                    }
                });
            }
        });

        deleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ArrayList<Integer> forDelte = myAdapterForDelete.getBeDelete();
                Collections.sort(forDelte);

                for (int i = forDelte.size() - 1; i >= 0; --i) {
                    int posForDialog = forDelte.get(i);
                    if (posForDialog != ListView.INVALID_POSITION) {
                        String str = arItem.get(posForDialog).name.getName();

                        arItem.remove(posForDialog);
                    }

                    final List<User> users = new ArrayList<User>();

                    for(int j=0; j < arItem.size(); ++j)
                    {
                        User user = arItem.get(j).name;
                        users.add(user);
                    }

                    complexObject.setUsers(users);

                    complexPreferences.putObject("list", complexObject);
                    complexPreferences.commit();

                    myList.clearChoices();
                    myAdapterForDelete.notifyDataSetChanged();

                    deleteTxt.setText("삭제");

                    LinearLayout listLayout2 = (LinearLayout) findViewById(R.id.list_menu2);
                    listLayout2.setVisibility(View.VISIBLE);

                    LinearLayout listLayout = (LinearLayout) findViewById(R.id.list_menu);
                    listLayout.setVisibility(View.GONE);

                    final MyListAdapter myAdapter = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext, arItem);

                    final ListView myList;
                    myList = (ListView) findViewById(R.id.list);
                    myList.setAdapter(myAdapter);

                    myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int pos, long id) {
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("삭제")
                                    .setMessage("정말 지우시겠어요?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            if (posForDialog != ListView.INVALID_POSITION) {
                                                String str = arItem.get(posForDialog).name.getName();
                                                complexPreferences.delete(complexObject, new User(str));
                                                complexPreferences.commit();

                                                arItem.remove(posForDialog);
                                                myList.clearChoices();
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                            return true;
                        }
                    });
                }
            }
        });

        orderByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) orderByDate.getBackground();
                int colorId = buttonColor.getColor();
                Log.e("색깔이 무엇일까?date  ", String.valueOf(colorId));  //-5054501

                if (colorId == -5054501) {

                } else {
                    orderByDate.setBackgroundColor(Color.parseColor("#b2dfdb"));
                    orderByName.setBackgroundColor(Color.parseColor("#ffffff"));

                    complexPreferences = ComplexPreferences.getComplexPreferences(FavoriteActivity.this, "mypref", MODE_PRIVATE);
                    complexObject = complexPreferences.getObject("list", ListUserPref.class);

                    List<User> sortedList;
                    if (complexObject == null)
                        return;
                    sortedList = complexObject.users;
                    if (sortedList == null)
                        return;
                    Collections.sort(sortedList, new CustomComparator(true)); // 시간순...

                    complexObject.setUsers(sortedList);
                    complexPreferences.putObject("list", complexObject);
                    complexPreferences.commit();

                    arItem = new ArrayList<MyItemForListView>();

                    // 리스트 뷰에 넣을 LIST 생성
                    MyItemForListView mi;
                    for (User item : complexObject.users) {
                        mi = new MyItemForListView(R.drawable.pin, item, R.drawable.arrow);
                        arItem.add(mi);
                    }

                    final MyListAdapter myAdapter = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext, arItem);

                    final ListView myList;
                    myList = (ListView) findViewById(R.id.list);
                    myList.setAdapter(myAdapter);

                    myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int pos, long id) {
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("삭제")
                                    .setMessage("정말 지우시겠어요?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            if (posForDialog != ListView.INVALID_POSITION) {
                                                String str = arItem.get(posForDialog).name.getName();
                                                complexPreferences.delete(complexObject, new User(str));
                                                complexPreferences.commit();

                                                arItem.remove(posForDialog);
                                                myList.clearChoices();
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                            return true;
                        }
                    });

                }
            }
        });

        orderByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDrawable buttonColor = (ColorDrawable) orderByName.getBackground();
                int colorId = buttonColor.getColor();
                Log.e("색깔이 무엇일까?name  ", String.valueOf(colorId)); //-1 :흰색

                if (colorId == -5054501) {

                } else {
                    orderByName.setBackgroundColor(Color.parseColor("#b2dfdb"));
                    orderByDate.setBackgroundColor(Color.parseColor("#ffffff"));

                    complexPreferences = ComplexPreferences.getComplexPreferences(FavoriteActivity.this, "mypref", MODE_PRIVATE);
                    complexObject = complexPreferences.getObject("list", ListUserPref.class);

                    List<User> sortedList;
                    if (complexObject == null)
                        return;
                    sortedList = complexObject.users;
                    if (sortedList == null)
                        return;
                    Collections.sort(sortedList, new CustomComparator(false)); // 이름순...

                    complexObject.setUsers(sortedList);
                    complexPreferences.putObject("list", complexObject);
                    complexPreferences.commit();

                    arItem = new ArrayList<MyItemForListView>();

                    // 리스트 뷰에 넣을 LIST 생성
                    MyItemForListView mi;
                    for (User item : complexObject.users) {
                        mi = new MyItemForListView(R.drawable.pin, item, R.drawable.arrow);
                        arItem.add(mi);
                    }

                    final MyListAdapter myAdapter = new MyListAdapter(FavoriteActivity.this, R.layout.widget_icontext, arItem);

                    final ListView myList;
                    myList = (ListView) findViewById(R.id.list);
                    myList.setAdapter(myAdapter);

                    myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                       int pos, long id) {
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("삭제")
                                    .setMessage("정말 지우시겠어요?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            if (posForDialog != ListView.INVALID_POSITION) {
                                                String str = arItem.get(posForDialog).name.getName();
                                                complexPreferences.delete(complexObject, new User(str));
                                                complexPreferences.commit();

                                                arItem.remove(posForDialog);
                                                myList.clearChoices();
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                            return true;
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frameForCloseFav:
                drawerLayoutFav.closeDrawer(Gravity.RIGHT);
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
