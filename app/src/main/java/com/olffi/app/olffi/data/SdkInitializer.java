package com.olffi.app.olffi.data;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;

/**
 * Created by gabrielmorin on 10/06/2016.
 */

public class SdkInitializer {
    public static void stormpath(Context context) {
        if (!Stormpath.isInitialized()) {
            StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                    .baseUrl("https://www.olffi.com/api")
                    .build();
            Stormpath.init(context, stormpathConfiguration);
        }
    }

    public static void facebook(Context context, final FacebookSdk.InitializeCallback callback) {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(context.getApplicationContext(), callback);
            AppEventsLogger.activateApp(context);
        } else {
            callback.onInitialized();
        }
    }
}
