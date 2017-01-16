package com.olffi.app.olffi;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.olffi.app.olffi.webapp.controllers.WebViewController;

public class WebAppActivity extends AppCompatActivity {

    private static final String TAG = WebAppActivity.class.getName();

    private WebViewController webViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_app_activity);
        setupCookies();
        View loadingView = findViewById(R.id.view_loading);
        View errorView = findViewById(R.id.view_error);
        WebView webView = (WebView) findViewById(R.id.webView);
        webViewController = new WebViewController(this, webView, loadingView, errorView);
    }

    public void loadUrl(String url) {
        webViewController.loadUrl(url);
        Log.i(this.getClass().getSimpleName(), "loading url: "+url);
    }

    @Override
    public void onBackPressed() {
        if (!webViewController.handlesBackPressed())
            super.onBackPressed();
    }

    private void setupCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        cookieManager.setAcceptCookie(true);
    }
}
