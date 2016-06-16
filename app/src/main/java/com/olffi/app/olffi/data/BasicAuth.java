package com.olffi.app.olffi.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gabrielmorin on 16/06/2016.
 */

public class BasicAuth {
    private final static String TAG = BasicAuth.class.getName();
    private final static String url = "https://www.olffi.com/api/user";
    public interface AuthResponse {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public static void logIn(final Context context, final String email, final String password, final AuthResponse callback) {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", Credentials.basic(email, password))
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Login in call error: ", e);
                callback.onFailure("Can't connect to the server, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseString = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseString);
                        String token = json.getString("token");
                        new UserPreferences(context).logInWithEmail(email, token);
                        callback.onSuccess();
                    } catch (JSONException e) {
                        String error = "Error parsing JSON: "+ e.getMessage();
                        Log.e(TAG, error, e);
                        callback.onFailure(error);
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public static void sigUp(String firstName, String lastName, String email, String password, String passwordConfirmation, final AuthResponse callback) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("first_name", firstName)
                .add("last_name", lastName)
                .add("user_email", email)
                .add("user_password", password)
                .add("user_password_confirmation", passwordConfirmation)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "SignUp call error: ", e);
                callback.onFailure("Can't connect to the server, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "SigUp response received: " + response.body().string());
                if (response.isSuccessful())
                    callback.onSuccess();
                else
                    callback.onFailure(response.message());
            }
        });
    }

    private Request addBasicAuthHeaders(Request request, String email, String password) {
        return request.newBuilder().header("Authorization", Credentials.basic(email, password)).build();
    }
}
