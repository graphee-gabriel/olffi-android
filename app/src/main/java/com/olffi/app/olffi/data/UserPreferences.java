package com.olffi.app.olffi.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

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

    private final String KEY_EMAIL = "email";
    private final String KEY_BA_TOKEN = "basic_auth_token";
    private final String KEY_LI_TOKEN = "li_token";
    private final String KEY_FB_TOKEN = "fb_token";
    private final String KEY_LOGIN_TYPE = "login_type";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public UserPreferences(Context context) {
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void logInWithEmail(String email, String token) {
        pref.edit()
                .putString(KEY_EMAIL, email)
                .putString(KEY_BA_TOKEN, token)
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_EMAIL)
                .apply();
    }

    public void logInWithLinkedIn(String token) {
        pref.edit()
                .putString(KEY_LI_TOKEN, token)
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_LINKED_IN)
                .apply();
    }

    public void logInWithFacebook(String token) {
        pref.edit()
                .putString(KEY_FB_TOKEN, token)
                .putInt(KEY_LOGIN_TYPE, LOGIN_TYPE_FACEBOOK)
                .apply();
    }


    public int getLoginType() {
        return pref.getInt(KEY_LOGIN_TYPE, LOGIN_TYPE_LOGGED_OUT);
    }

    public String getTokenLinkedIn() {
        return pref.getString(KEY_LI_TOKEN, "");
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    public String getTokenBasicAuth() {
        return pref.getString(KEY_BA_TOKEN, "");
    }

    public String getTokenFacebook() {
        return pref.getString(KEY_FB_TOKEN, "");
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

    public String getCredentialLogin() {
        int loginType = getLoginType();
        switch (loginType) {
            case LOGIN_TYPE_EMAIL :
                return "basic";
            case LOGIN_TYPE_FACEBOOK :
                return "facebook";
            case LOGIN_TYPE_LINKED_IN :
                return "linkedin";
            default: return "";
        }
    }

    public String getCredentialPassword() {
        int loginType = getLoginType();
        switch (loginType) {
            case LOGIN_TYPE_EMAIL :
                return getTokenBasicAuth();
            case LOGIN_TYPE_FACEBOOK :
                return getTokenFacebook();
            case LOGIN_TYPE_LINKED_IN :
                return getTokenLinkedIn();
            default: return "";
        }
    }
}
