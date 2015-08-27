package com.snumap.snumapversion1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DeleteRecentSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_recent_search);
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
