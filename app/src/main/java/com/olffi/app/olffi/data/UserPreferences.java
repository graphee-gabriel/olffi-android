package com.olffi.app.olffi.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.linkedin.platform.AccessToken;
import com.olffi.app.olffi.BuildConfig;

/**
 * Created by gabrielmorin on 09/06/2016.
 */

public class UserPreferences {
    SharedPreferences pref;
    public final int
            LOGIN_TYPE_LOGGED_OUT = -1,
            LOGIN_TYPE_EMAIL = 0,
            LOGIN_TYPE_LINKED_IN = 1,
            LOGIN_TYPE_FACEBOOK = 2;

    private final String PREF_NAME = BuildConfig.APPLICATION_ID;
    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "pw";
    private final String KEY_LI_TOKEN = "li_token";
    private final String KEY_LOGIN_TYPE = "login_type";

    public UserPreferences(Context context) {
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void logInWithEmail(String email, String password) {
        pref.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_PASSWORD, password)
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_EMAIL)
                .apply();
    }

    public void logInWithLinkedIn(AccessToken accessToken) {
        pref.edit()
                .putString(KEY_LI_TOKEN, new Gson().toJson(accessToken))
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_LINKED_IN)
                .apply();
    }

    public void logInWithFacebook() {
        pref.edit()
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_FACEBOOK)
                .apply();
    }


    public int getLoginType() {
        return pref.getInt(KEY_LOGIN_TYPE, LOGIN_TYPE_LOGGED_OUT);
    }

    public AccessToken getAccessTokenLinkedIn() {
        return new Gson().fromJson(getAccessTokenLinkedJson(), AccessToken.class);
    }

    private String getAccessTokenLinkedJson() {
        return pref.getString(KEY_LI_TOKEN, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return pref.getString(KEY_PASSWORD, "");
    }

    public boolean isLoggedIn() {
        return getLoginType() != LOGIN_TYPE_LOGGED_OUT;
    }

    public boolean isLoggedInWithEmail() {
        return getLoginType() == LOGIN_TYPE_EMAIL;
    }

    public boolean isLoggedInWithLinkedIn() {
        return getLoginType() == LOGIN_TYPE_LINKED_IN;
    }

    public boolean isLoggedInWithFacebook() {
        return getLoginType() == LOGIN_TYPE_FACEBOOK;
    }

    public void logOut() {
        if (FacebookSdk.isInitialized())
            LoginManager.getInstance().logOut();
        pref.edit().clear().apply();
    }
}
