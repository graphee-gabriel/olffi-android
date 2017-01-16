package com.olffi.app.olffi.webapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.olffi.app.olffi.webapp.Navigator;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class WebViewController {
    private final static String TAG = WebViewController.class.getSimpleName();
    private WebView webView;
    private View loadingView, errorView;
    private Activity activity;
    private boolean shouldShowErrorView = false;

    public WebViewController(Activity activity, WebView webView, View loadingView, View errorView) {
        this.activity = activity;
        this.webView = webView;
        this.loadingView = loadingView;
        this.errorView = errorView;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                shouldShowErrorView = true;
                Log.e(TAG, "onReceivedError: "+description);
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                onUrlChange(activity, view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (shouldShowErrorView) {
                    shouldShowErrorView = false;
                    show(errorView);
                } else {
                    show(webView);
                }
                //Log.e(TAG, "onPageFinished: "+url);
            }
        };
        webView.setWebViewClient(webViewClient);
    }

    private void onUrlChange(Activity activity, WebView view, String url) {
        Navigator.handlesUrl(activity, view, url);
    }

    public boolean handlesBackPressed() {
        return Navigator.handlesBackPressed(webView);
    }

    public void loadUrl(String url) {
        show(loadingView);
        webView.loadUrl(url);
    }

    private void show(View view) {
        for (View v : new View[]{loadingView, errorView, webView}) {
            v.setVisibility(v == view ? View.VISIBLE : View.GONE);
        }
    }
}
