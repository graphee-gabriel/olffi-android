package com.olffi.app.olffi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;
import com.olffi.app.olffi.notifications.RegistrationIntentService;

public class WebAppActivity extends AppCompatActivity {

    private static final String TAG = WebAppActivity.class.getName();
    private WebView webView;
    private View loading;
    private ProgressBar progressBarInApp;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_app_activity);
        UserPreferences pref = new UserPreferences(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(UserPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

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
                    super.onLoadResource(view, url);
                    if (url.contains("logout")) {
                        new UserPreferences(WebAppActivity.this).logOut();
                        App.startActivityClearTop(WebAppActivity.this, MainActivity.class);
                    }
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

            String credentials = "";
            if (pref.isLoggedInWithFacebook()) {
                credentials = getCredentials(AccessToken.getCurrentAccessToken().getToken(), "facebook");
            } else if (pref.isLoggedInWithLinkedIn()) {
                credentials = getCredentials(pref.getTokenLinkedIn(), "linkedin");
            } else if (pref.isLoggedInWithEmail()) {
                credentials = getCredentials(pref.getTokenBasicAuth(), "basic");
            }
            String url = "https://www.olffi.com/app"+credentials;
            webView.loadUrl(url);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(UserPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }




    private void showLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.GONE);
    }

    private String getCredentials(String token, String type) {
        if (token.isEmpty() || type.isEmpty())
            return "";
        return "?token=" + token + "&type=" + type;
    }
}
