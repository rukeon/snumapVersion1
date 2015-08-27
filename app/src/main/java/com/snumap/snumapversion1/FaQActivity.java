package com.snumap.snumapversion1;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FaQActivity extends AppCompatActivity {
    private WebView mywebView;
    private ImageView gotoBack;

    private ProgressBar myprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = ni.isConnected();

        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobileConn = ni.isConnected();

        if (!isWifiConn && !isMobileConn) {
            Toast.makeText(this, "인터넷에 접속되어 있지 않습니다!", Toast.LENGTH_SHORT)
                    .show();
            finish();//액티비티 종료
        } else {
            setContentView(R.layout.activity_fa_q);

            myprogress = (ProgressBar) findViewById(R.id.myweb_progress);

            mywebView = (WebView) findViewById(R.id.mywebView);
            gotoBack = (ImageView) findViewById(R.id.gotoBack);

            mywebView.setWebViewClient(new mywebClient());
            mywebView.getSettings().setJavaScriptEnabled(true);
            mywebView.loadUrl("http://www.snumap.com/snumap/#page2");

            gotoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    public class mywebClient extends WebViewClient
    {
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            myprogress.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
    }
}
