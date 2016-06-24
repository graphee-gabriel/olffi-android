package com.olffi.app.olffi.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.olffi.app.olffi.R;
import com.olffi.app.olffi.data.Auth;
import com.olffi.app.olffi.data.UserPreferences;

import java.io.IOException;

/**
 * Created by gabrielmorin on 24/06/2016.
 */

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(UserPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        if (token != null)
            sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(final String token) {
        // Add custom implementation, as needed.
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        NotificationToken.send(this, token, new Auth.AuthResponse() {
            @Override
            public void onSuccess() {
                sharedPreferences.edit().putBoolean(UserPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                try {
                    subscribeTopics(token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent registrationComplete = new Intent(UserPreferences.REGISTRATION_COMPLETE);
                LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
                Log.d(TAG, "sendRegistrationToServer SUCCESS!");
            }

            @Override
            public void onFailure(String errorMessage) {
                sharedPreferences.edit().putBoolean(UserPreferences.SENT_TOKEN_TO_SERVER, false).apply();
                Log.d(TAG, "sendRegistrationToServer failure...");
            }
        });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
