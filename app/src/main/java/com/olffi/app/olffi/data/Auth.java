package com.olffi.app.olffi.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

public class Auth {
    private final static String TAG = Auth.class.getName();
    private final static String urlBasic = "https://www.olffi.com/api/user";
    public interface AuthResponse {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static void logIn(final Context context, final String email, final String password, final AuthResponse callback) {
        Request request = new Request.Builder()
                .url(urlBasic)
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
                /*if (response.isSuccessful()) {
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
                }*/

                String token = parseToken(response);
                if (token == null) {
                    callback.onFailure(response.message());
                } else {
                    new UserPreferences(context).logInWithEmail(email, token);
                    callback.onSuccess();
                }
            }
        });
    }

    public static void signUp(String firstName, String lastName, String email, String password, final AuthResponse callback)  {
        RequestBody body = new FormBody.Builder()
                .add("first_name", firstName)
                .add("last_name", lastName)
                .add("user_email", email)
                .add("user_password", password)
                .build();

        Request request = new Request.Builder()
                .url(urlBasic)
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

    public static void linkedIn(final Context context, String id, String firstName, String lastName, String email, final AuthResponse callback)  {
        String hash = null;
        try {
            String salt = "FuckL1nk3dIN.com";
            hash = md5(firstName + lastName + email + id + salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (hash == null) {
            callback.onFailure("Authentication error");
            return;
        }
        Log.d(TAG, "hash: "+hash);
        RequestBody body = new FormBody.Builder()
                .add("linkedin_id", id)
                .add("first_name", firstName)
                .add("last_name", lastName)
                .add("user_email", email)
                .add("hash", hash)
                .build();

        Request request = new Request.Builder()
                .url(urlBasic)
                .post(body)
                .build();

        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "linkedIn call error: ", e);
                callback.onFailure("Can't connect to the server, please try again later.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String token = parseToken(response);
                if (token == null) {
                    callback.onFailure(response.message());
                } else {
                    new UserPreferences(context).logInWithLinkedIn(token);
                    callback.onSuccess();
                }
            }
        });
    }
    private static String parseToken(Response response) throws IOException {
        String token = null;
        if (response.isSuccessful()) {
            String responseString = response.body().string();
            Log.d(TAG, "token response received: " + responseString);
            try {
                JSONObject json = new JSONObject(responseString);
                token = json.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return token;
    }
    private static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}
