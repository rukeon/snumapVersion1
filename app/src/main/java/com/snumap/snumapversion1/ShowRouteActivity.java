package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.snumap.snumapversion1.Model.RouteModel;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowRouteActivity extends AppCompatActivity {
    Intent mIntent;
    String from;
    String to;

    TextView fromtxt;
    TextView totxt;

    // db 처리하는 부분
    public MyDatabase db;

    private RouteService routeService;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        routeService = new RouteService();

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

        routeService.getRouteApi().getRoute(idxFrom, idxTo, new Callback<RouteModel>() {
            @Override
            public void success(RouteModel routeModel, Response response) {
                latitude = routeModel.getLatitude();
                longitude = routeModel.getLongitude();

                fromtxt.setText(latitude);
                totxt.setText(longitude);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
