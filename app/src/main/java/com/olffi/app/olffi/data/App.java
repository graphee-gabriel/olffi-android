package com.olffi.app.olffi.data;

import android.app.Activity;
import android.content.Intent;

import com.olffi.app.olffi.CountrySearchActivity;
import com.olffi.app.olffi.CredentialsWebAppActivity;
import com.olffi.app.olffi.LoadUrlWebAppActivity;
import com.olffi.app.olffi.MenuActivity;
import com.olffi.app.olffi.SearchActivity;

/**
 * Created by gabrielmorin on 10/06/2016.
 */

public class App {
    public static final String INTENT_EXTRA_IS_SIGN_UP = "is_sign_up";
    public static final String INTENT_EXTRA_URL = "url";
    public static final String INTENT_EXTRA_TITLE = "title";

    public static void startWebApp(Activity activity) {
        Intent intent = new Intent(activity, CredentialsWebAppActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }

    public static void startMenu(Activity activity) {
        Intent intent = new Intent(activity, MenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }

    public static void startSearch(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public static void startCountryList(Activity activity) {
        Intent intent = new Intent(activity, CountrySearchActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startWebAppFromUrl(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, LoadUrlWebAppActivity.class)
                .putExtra(INTENT_EXTRA_URL, url)
                .putExtra(INTENT_EXTRA_TITLE, title);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public static void startActivityClearTop(Activity currentActivity, Class newActivity) {
        Intent intent = new Intent(currentActivity, newActivity);
        startActivityClearTop(currentActivity, intent);
    }

    static void startActivityClearTop(Activity currentActivity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity.finish();
        currentActivity.startActivity(intent);
    }
}
