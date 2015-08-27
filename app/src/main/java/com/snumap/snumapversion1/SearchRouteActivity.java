package com.snumap.snumapversion1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchRouteActivity extends AppCompatActivity {
    TextView txtDelete;
    Button btnChange;
    FloatingActionButton fab;

    private InputMethodManager imm; // for keybaord control

    // db 처리하는 부분
    public MyDatabase db;

    CustomAutoCompleteView myAutoComplete;
    CustomAutoCompleteView myAutoComplete2;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;
    ArrayAdapter<String> myAdapter2;

    // just to add some initial value
    ArrayList<String> item = new ArrayList<String>();
    ArrayList<String> item2 = new ArrayList<String>();

    // 초성 검색을 위한 arrayList
    ArrayList<String> searchList = new ArrayList<String>();

    ArrayList<DataSet> search_result = new ArrayList<DataSet>();

    boolean isAutoCompleteExist1 = false;
    boolean isAutoCompleteExist2 = false;

//    // 뒤로가기 컨트롤
//    BackPressCloseHandler backPressCloseHandler;
//    Intent mIntent;
//    String previousActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route);

//        backPressCloseHandler = new BackPressCloseHandler(SearchRouteActivity.this);
//
//        mIntent = getIntent();
//        Bundle b = mIntent.getExtras();
//
//        if(b != null) {
//            previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
//        }

        // 키보드 관리를 위한 시작, 아래 함수 있다.
        init();

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);
        addList();//검색리스트를 애드 해준다

        // autocompletetextview is in activity_main.xml
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR);
        myAutoComplete2 = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR2);

        // add the listener so it will tries to suggest while the user types
        myAutoComplete.addTextChangedListener(new SRCustomAutoCompleteTextChangedListener(this));
        myAutoComplete2.addTextChangedListener(new SRCustomAutoCompleteTextChangedListener2(this));

        // set our adapter
        myAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_row, item);
        myAutoComplete.setAdapter(myAdapter); //....여기까지(디비)

        // set our adapter
        myAdapter2 = new ArrayAdapter<String>(this, R.layout.autocomplete_list_row, item2);
        myAutoComplete2.setAdapter(myAdapter2); //....여기까지(디비)

        // 자동검색 바 클릭시 검색어 없애기
        myAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAutoComplete.setText("");
                showKeyboard();
            }
        });

        // 자동검색 바 클릭시 검색어 없애기
        myAutoComplete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAutoComplete2.setText("");
                showKeyboard2();
            }
        });

        // 자동 검색 리스트에서 검색어 클릭 시 해당 위치로 이동
        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isAutoCompleteExist1 = true;
                hideKeyboard();

                if (isAutoCompleteExist2)
                {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        // 자동 검색 리스트에서 검색어 클릭 시 해당 위치로 이동
        myAutoComplete2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                isAutoCompleteExist2 = true;
                hideKeyboard();
                if (isAutoCompleteExist1)
                {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        txtDelete = (TextView) findViewById(R.id.txtDelete);
        btnChange = (Button) findViewById(R.id.btnChange);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        String text = "최근 검색기록 <font color='red'>삭제</font>";
        txtDelete.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteRoute = new Intent(SearchRouteActivity.this, DeleteRecentSearchActivity.class);
                startActivity(deleteRoute);
                overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
            }
        });
    }

    private void addList() {
        searchList = db.insertBuildingName();
    }

    private void init(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR);
        myAutoComplete2 = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR2);
    }

    private void showKeyboard(){
        imm.showSoftInput(myAutoComplete, 0);
    }

    private void showKeyboard2(){
        imm.showSoftInput(myAutoComplete2, 0);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    @Override
//    public void onBackPressed() {
//        backPressCloseHandler.onBackPressed();
//    }
}
