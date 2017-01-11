package com.olffi.app.olffi.webapp.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.olffi.app.olffi.webapp.Navigator;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class WebViewController {
    private WebView webView;
    private View loadingView;
    private ProgressBarController progressBarController;
    private Activity activity;

    public WebViewController(Activity activity, WebView webView, View loading, ProgressBarController progressBarController) {
        this.activity = activity;
        this.webView = webView;
        this.loadingView = loading;
        this.progressBarController = progressBarController;
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        webView.getSettings().setJavaScriptEnabled(true);
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                onUrlChange(activity, view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //view.loadUrl("javascript:init('" + theArgumentYouWantToPass + "')");
                if (loadingView.getVisibility() == View.VISIBLE)
                    loadingView.setVisibility(View.GONE);
                progressBarController.hideLoading();
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
        webView.loadUrl(url);
    }
}
