package com.snumap.snumapversion1;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by rukeon01 on 2015-07-15.
 */
public class CustomAutoCompleteTextChangedListener implements TextWatcher {
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
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
        Log.e(TAG, "User input: " + userInput);

        MainActivity mainActivity = ((MainActivity) context);

        // setOnItemClickListener와의 겹치는 것 제한하기 위한 코드
        if (mainActivity.myAutoComplete.isPerformingCompletion()) {
            // An item has been selected from the list. Ignore.
            return;
        }

        // query the database based on the user input
        mainActivity.item = mainActivity.db.read(userInput.toString());

        if (mainActivity.item.size() == 0 ) {
            Log.e("여기로 들어오나?", "real?");
            for (int i = 0; i < mainActivity.searchList.size(); i++) {
                String searchData = mainActivity.searchList.get(i).toString();
                String keyWord = userInput.toString();
                boolean isData = SoundSearch.matchString(searchData, keyWord); //검색할 대상 , 검색 키워드로  검색키워드가 검색대상에 있으면  true를 리턴해준다
                if (isData) {
                    mainActivity.item.add(searchData);//검색대상에 있으면 새로운 리스트를 만들어서 이름을 애드해준다
                }
            }
        }

        // update the adapater
        mainActivity.myAdapter.notifyDataSetChanged();
        mainActivity.myAdapter = new ArrayAdapter<String>(mainActivity, R.layout.autocomplete_list_row, mainActivity.item);
        mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);

    }
}
