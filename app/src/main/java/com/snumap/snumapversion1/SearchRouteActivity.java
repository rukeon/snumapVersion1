package com.snumap.snumapversion1;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

public class SearchRouteActivity extends AppCompatActivity {
    TextView txtDelete;
    Button btnChange;
    FloatingActionButton fab;

    private InputMethodManager imm; // for keybaord control

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
        setContentView(R.layout.activity_search_route);

        // 키보드 관리를 위한 시작, 아래 함수 있다.
        init();

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);
        addList();//검색리스트를 애드 해준다

        // autocompletetextview is in activity_main.xml
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR);

        // add the listener so it will tries to suggest while the user types
        myAutoComplete.addTextChangedListener(new SRCustomAutoCompleteTextChangedListener(this));

        // set our adapter
        myAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_row, item);
        myAutoComplete.setAdapter(myAdapter); //....여기까지(디비)

        // 자동검색 바 클릭시 검색어 없애기
        myAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAutoComplete.setText("");
                showKeyboard();
            }
        });

        // 자동 검색 리스트에서 검색어 클릭 시 해당 위치로 이동
        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                hideKeyboard();
            }
        });

        txtDelete = (TextView) findViewById(R.id.txtDelete);
        btnChange = (Button) findViewById(R.id.btnChange);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        String text = "최근 검색기록 <font color='red'>삭제</font>";
        txtDelete.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addList() {
        searchList = db.insertBuildingName();
    }

    private void init(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myAutoCompleteSR);
    }

    private void showKeyboard(){
        imm.showSoftInput(myAutoComplete, 0);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
