package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowRouteActivity extends AppCompatActivity {
    Intent mIntent;
    String from;
    String to;

    TextView fromtxt;
    TextView totxt;

    // db 처리하는 부분
    public MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        // 디비 처리 하는 부분 시작
        db = new MyDatabase(this);

        fromtxt = (TextView) findViewById(R.id.from);
        totxt = (TextView) findViewById(R.id.to);

        mIntent = getIntent();
        Bundle b = mIntent.getExtras();

        if(b != null) {
            from = mIntent.getStringExtra("FROM");
            to = mIntent.getStringExtra("TO");
        }

        DataSet resultFrom = db.selectDatabyId(from);
        DataSet resultTo = db.selectDatabyId(to);

        // 추후에 number에 idx를 넣자
        String idxFrom = resultFrom.getName();
        String idxTo = resultTo.getName();

        fromtxt.setText(idxFrom);
        totxt.setText(idxTo);
    }
}
