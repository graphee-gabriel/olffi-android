package com.olffi.app.olffi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.olffi.app.olffi.notifications.RegistrationIntentService;
import com.olffi.app.olffi.webapp.controllers.ProgressBarController;
import com.olffi.app.olffi.webapp.controllers.WebViewController;

public class WebAppActivity extends AppCompatActivity {

    private static final String TAG = WebAppActivity.class.getName();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private WebViewController webViewController;
    private ProgressBarController progressBarController;
    //private BroadcastReceiver mRegistrationBroadcastReceiver;
    //private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_app_activity);

        setupCookies();
        //setupReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        View loadingView = findViewById(R.id.loadingView);
        WebView webView = (WebView) findViewById(R.id.webView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarInApp);
        //TextView titleTextView = (TextView) loadingView.findViewById(R.id.title);
        //titleTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        progressBarController = new ProgressBarController(progressBar);
        webViewController = new WebViewController(this, webView, loadingView, progressBarController);
    }

    public void loadUrl(String url) {
        progressBarController.showLoading();
        webViewController.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (!webViewController.handlesBackPressed())
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver();
    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        //isReceiverRegistered = false;
        super.onPause();
    }

    private void setupCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        cookieManager.setAcceptCookie(true);
    }
/*
    private void setupReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                *//*SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(UserPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                } else {
                }*//*
            }
        };
    }

    private void registerReceiver() {
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(UserPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }*/

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
}
