package com.olffi.app.olffi;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;

public class WebAppActivity extends AppCompatActivity {

    private static final String TAG = WebAppActivity.class.getName();
    private WebView webView;
    private View loading;
    private ProgressBar progressBarInApp;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_app_activity);
        UserPreferences pref = new UserPreferences(this);
        progressBarInApp = (ProgressBar) findViewById(R.id.progressBarInApp);
        loading = findViewById(R.id.loading);
        webView = (WebView) findViewById(R.id.webView);

        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        cookieManager.setAcceptCookie(true);
        if (progressBarInApp != null) {
            progressBarInApp.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            progressBarInApp.getProgressDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            showLoading();
        }

        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            WebViewClient webViewClient = new WebViewClient() {

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(WebAppActivity.this, description, Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onLoadResource(WebView view, String url) {
                    if (url.contains("logout")) {
                        new UserPreferences(WebAppActivity.this).logOut();
                        App.startActivityClearTop(WebAppActivity.this, MainActivity.class);
                    }
                    super.onLoadResource(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    //view.loadUrl("javascript:init('" + theArgumentYouWantToPass + "')");
                    if (loading.getVisibility() == View.VISIBLE)
                        loading.setVisibility(View.GONE);
                    hideLoading();
                }
            };
            webView.setWebViewClient(webViewClient);

//            String credentials = "login_email=toto";
//            Log.d(TAG, credentials);
//            webView.postUrl("http://www.olffi.com/gm/index.php", credentials.getBytes());
            String credentials = "";
            if (pref.isLoggedInWithFacebook()) {
                credentials = "?token="+ AccessToken.getCurrentAccessToken().getToken()+"&type=facebook";
            } else if (pref.isLoggedInWithLinkedIn()) {
                credentials = "?token="+ pref.getAccessTokenLinkedIn().getValue()+"&type=linkedin";
            } else if (pref.isLoggedInWithEmail()) {
                credentials = "";
            }
            webView.loadUrl("https://www.olffi.com/app"+credentials);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.GONE);
    }
}
