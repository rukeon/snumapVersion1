package com.snumap.snumapversion1;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.Collections;

/**
 * Created by rukeon01 on 2015-07-15.
 */
public class SRCustomAutoCompleteTextChangedListener implements TextWatcher {
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public SRCustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {

        // 사용자가 엔터 입력했을 때 자동으로 제거해주는 코드
        for(int i = s.length(); i > 0; i--){
            if(s.subSequence(i-1, i).toString().equals("\n"))
                s.replace(i-1, i, "");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // if you want to see in the logcat what the user types
//        Log.e(TAG, "User input: " + userInput);

        SearchRouteActivity searchActivity = ((SearchRouteActivity) context);
        // setOnItemClickListener와의 겹치는 것 제한하기 위한 코드
        if (searchActivity.myAutoComplete.isPerformingCompletion()) {
            // An item has been selected from the list. Ignore.
            return;
        }

        // query the database based on the user input
        searchActivity.item = searchActivity.db.readExceptSlang(userInput.toString());

        if (searchActivity.item.size() == 0 ) {
            for (int i = 0; i < searchActivity.searchList.size(); i++) {
                String searchData = searchActivity.searchList.get(i).toString();
                String keyWord = userInput.toString();
                boolean isData = SoundSearch.matchString(searchData, keyWord); //검색할 대상 , 검색 키워드로  검색키워드가 검색대상에 있으면  true를 리턴해준다
                if (isData) {
                    searchActivity.item.add(searchData);//검색대상에 있으면 새로운 리스트를 만들어서 이름을 애드해준다
                }
            }
        }
        // 추가한 코드
        Collections.sort(searchActivity.item);

        // update the adapater
        searchActivity.myAdapter.notifyDataSetChanged();
        searchActivity.myAdapter = new ArrayAdapter<String>(searchActivity, R.layout.autocomplete_list_row, searchActivity.item);
        searchActivity.myAutoComplete.setAdapter(searchActivity.myAdapter);

        searchActivity.isAutoCompleteExist1 = false;
        searchActivity.fab.setVisibility(View.GONE);
    }
}
