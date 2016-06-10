package com.olffi.app.olffi.data;

import android.app.Activity;
import android.content.Intent;

import com.olffi.app.olffi.WebAppActivity;

/**
 * Created by gabrielmorin on 10/06/2016.
 */

public class App {
    public static final String INTENT_EXTRA_IS_SIGN_UP = "is_sign_up";

    public static void startWebApp(Activity activity) {
        Intent intent = new Intent(activity, WebAppActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }



    public static void startActivityClearTop(Activity currentActivity, Class newActivity) {
        Intent intent = new Intent(currentActivity, newActivity);
        startActivityClearTop(currentActivity, intent);
    }

    public static void startActivityClearTop(Activity currentActivity, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity.finish();
        currentActivity.startActivity(intent);
    }
}
