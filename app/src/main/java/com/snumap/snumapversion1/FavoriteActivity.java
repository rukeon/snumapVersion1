package com.snumap.snumapversion1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    ComplexPreferences complexPreferences;
    ListUserPref complexObject;

    ArrayList<MyItemForListView> arItem;

    TextView deleteTxt;
    TextView cancelTxt;
    TextView deleteTxtTogo;

    MyListAdapter myAdapterForDelete;

    Button orderByDate;
    Button orderByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

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
                    String str = arItem.get(position).name.getName();
                    goToMainintent.putExtra("favorite", str);
                    startActivity(goToMainintent);
                }
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
//                Log.v("long clicked", "pos: " + pos);
//                String str = arItem.get(pos).name + "를 주문합니다.";
                final int posForDialog = pos;

                new AlertDialog.Builder(FavoriteActivity.this)
                        .setTitle("Title")
                        .setMessage("Do you really want to whatever?")
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
//                        Log.v("long clicked", "pos: " + pos);
//                        String str = arItem.get(pos).name + "를 주문합니다.";
                        final int posForDialog = pos;

                        new AlertDialog.Builder(FavoriteActivity.this)
                                .setTitle("Title")
                                .setMessage("Do you really want to whatever?")
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

//                for(int i=0; i < arItem.size(); ++i)
//                {
//                    String str = arItem.get(i).name;
//                    Log.e("현재 arItem의 상태다: ", str);
//                }

                Collections.sort(forDelte);

//                for (int k : forDelte)
//                {
//                    Log.e("forDelte의 상태:   ", String.valueOf(k));
//                }

                for (int i = forDelte.size() - 1; i >= 0; --i) {
                    int posForDialog = forDelte.get(i);

//                    Log.e("어떤 숫자가 들어가 있을까? ", String.valueOf(posForDialog));

                    if (posForDialog != ListView.INVALID_POSITION) {
                        String str = arItem.get(posForDialog).name.getName();
//                        complexPreferences.delete(complexObject, new User(str));
//                        complexPreferences.commit();

                        arItem.remove(posForDialog);

//                        MyItemForListView mi;
//                        mi = new MyItemForListView(R.drawable.pin, str, R.drawable.arrow);
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

//                    complexObject.setUsers(arItem);
//                    arItem = new ArrayList<MyItemForListView>();
//                    // 리스트 뷰에 넣을 LIST 생성
//                    MyItemForListView mi;
//                    for(User item: complexObject.users){
//                        mi = new MyItemForListView(R.drawable.pin, item.getName(), R.drawable.arrow);
//                        arItem.add(mi);
//                    }
//
                    myList.clearChoices();
                    myAdapterForDelete.notifyDataSetChanged();

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
//                            Log.v("long clicked", "pos: " + pos);
//                            String str = arItem.get(pos).name + "를 주문합니다.";
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("Title")
                                    .setMessage("Do you really want to whatever?")
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
                    sortedList = complexObject.users;
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
//                Log.v("long clicked", "pos: " + pos);
//                String str = arItem.get(pos).name + "를 주문합니다.";
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("Title")
                                    .setMessage("Do you really want to whatever?")
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
                    sortedList = complexObject.users;
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
//                Log.v("long clicked", "pos: " + pos);
//                String str = arItem.get(pos).name + "를 주문합니다.";
                            final int posForDialog = pos;

                            new AlertDialog.Builder(FavoriteActivity.this)
                                    .setTitle("Title")
                                    .setMessage("Do you really want to whatever?")
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
}
