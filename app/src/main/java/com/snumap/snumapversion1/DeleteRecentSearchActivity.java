package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteRecentSearchActivity extends AppCompatActivity {
    // SharedPreference 처리
    List<UserSearch> userSearch;
    ListUserSearchPref complexObject;
    ComplexPreferences complexPreferences;

    MyListAdapterForDR myListAdapterForDR;

    ArrayList<MyItemForListViewDR> arItem;

    TextView deleteTxtDR;
    TextView cancelTxtDR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_recent_search);

        deleteTxtDR = (TextView) findViewById(R.id.deleteTxtDR);
        cancelTxtDR = (TextView) findViewById(R.id.cancelTxtDR);

        // sharedpreference 싱글톤으로 가져오기
        complexPreferences = ComplexPreferences.getComplexPreferences(this, "mypref", MODE_PRIVATE);
        complexObject = complexPreferences.getObject("listForSearch", ListUserSearchPref.class); // null일수 있다.

        // 본 내용의 list 부분 시작...
        arItem = new ArrayList<MyItemForListViewDR>();

        if (complexObject != null) {
            // 시간순 정렬을 위한....
            List<UserSearch> sortedList;
            sortedList = complexObject.search;
            if (sortedList == null)
                return;
            Collections.sort(sortedList, new CustomComparatorSR()); // 시간순...

            complexObject.setUserSearch(sortedList);
            complexPreferences.putObject("listForSearch", complexObject);
            complexPreferences.commit();
        }

        // 리스트 뷰에 넣을 LIST 생성
        MyItemForListViewDR mi;
        if (complexObject != null)
        {
            for(UserSearch item: complexObject.search){
                mi = new MyItemForListViewDR(R.drawable.mybutton_dr, item, R.drawable.arrow_forward);
                arItem.add(mi);
            }
        }

        final MyListAdapterForDR myListAdapterForDR = new MyListAdapterForDR(DeleteRecentSearchActivity.this, R.layout.widget_icontext_dr, arItem);

        final ListView myList;
        myList = (ListView) findViewById(R.id.listDR);
        myList.setEmptyView(findViewById(R.id.txtForNothingExistDR));
        myList.setAdapter(myListAdapterForDR);

        deleteTxtDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> forDelte = myListAdapterForDR.getBeDelete();
                Collections.sort(forDelte);

                for (int i : forDelte) {
                    Log.e("beDelete의 상태 ACTIVITY:", String.valueOf(i));
                }

                for (int i = forDelte.size() - 1; i >= 0; --i) {
                    int posForDialog = forDelte.get(i);
                    if (posForDialog != ListView.INVALID_POSITION) {
                        arItem.remove(posForDialog);
                    }

                    final List<UserSearch> userSearch = new ArrayList<UserSearch>();

                    for (int j = 0; j < arItem.size(); ++j) {
                        UserSearch user = arItem.get(j).search;
                        userSearch.add(user);
                    }

                    complexObject.setUserSearch(userSearch);

                    complexPreferences.putObject("listForSearch", complexObject);
                    complexPreferences.commit();

                    myListAdapterForDR.notifyDataSetChanged();
                    myList.clearChoices();
                    myListAdapterForDR.setBeDelete(new ArrayList<Integer>());
                    myListAdapterForDR.setNum(0);

                    deleteTxtDR.setText("삭제");

                    int childNum = myList.getChildCount();
                    for(int j = 0; j < childNum; ++j)
                    {
                        myList.getChildAt(j).findViewById(R.id.deleteBtn).setActivated(false);
                    }
                }
            }
        });

        cancelTxtDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_go_back = new Intent(DeleteRecentSearchActivity.this, SearchRouteActivity.class);
                intent_go_back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_go_back);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });
    }

    // 뒤로가기 버튼 컨트롤
    @Override
    public void onBackPressed() {
        Intent intent_go_back = new Intent(DeleteRecentSearchActivity.this, SearchRouteActivity.class);
        intent_go_back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_go_back);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
