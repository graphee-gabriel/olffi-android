package com.olffi.app.olffi.notifications;

import android.content.Context;
import android.util.Log;

import com.olffi.app.olffi.data.Auth;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gabrielmorin on 24/06/2016.
 */

class NotificationToken {

    private final static String url = "https://www.olffi.com/api/token";
    private static final String TAG = NotificationToken.class.getName();

    static void send(Context context, String token, final Auth.AuthResponse callback)  {
        RequestBody body = new FormBody.Builder()
                .add("type", "notification")
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader(Auth.HEADER_CREDENTIALS, Auth.getCredentials(context))
                .post(body)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Notification Token call error: ", e);
                callback.onFailure("Can't connect to the server, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Notification Token response received: " + response.body().string());
                if (response.isSuccessful())
                    callback.onSuccess();
                else
                    callback.onFailure(response.message());
            }
        });
    }
}
