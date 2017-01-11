package com.olffi.app.olffi.webapp;

import android.app.Activity;
import android.webkit.WebView;

import com.olffi.app.olffi.MainActivity;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class Navigator {

    public static boolean handlesUrl(Activity activity, WebView view, String url) {
        if (url.contains("logout")) {
            new UserPreferences(activity).logOut();
            App.startActivityClearTop(activity, MainActivity.class);
            return true;
        }

        return false;
    }

    public static boolean handlesBackPressed(WebView webView) {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return false;
    }
}
