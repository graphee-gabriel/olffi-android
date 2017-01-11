package com.olffi.app.olffi.webapp;

import com.facebook.AccessToken;
import com.olffi.app.olffi.BuildConfig;
import com.olffi.app.olffi.data.UserPreferences;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class UrlBuilder {

    public static String getUrlWebAppWithCredentials(UserPreferences pref) {
        return appendCredentialsToUrl(pref, BuildConfig.BASE_URL+"/app?");
    }

    public static String getUrlFromRelativeUrl(UserPreferences pref, String relativeUrl) {
        return appendCredentialsToUrl(pref, "http://www.olffi.com"+relativeUrl+"?body=1&");
    }

    public static String appendCredentialsToUrl(UserPreferences pref, String url) {
        String credentials = "";
        if (pref.isLoggedInWithFacebook()) {
            credentials = getCredentials(AccessToken.getCurrentAccessToken().getToken(), "facebook");
        } else if (pref.isLoggedInWithLinkedIn()) {
            credentials = getCredentials(pref.getTokenLinkedIn(), "linkedin");
        } else if (pref.isLoggedInWithEmail()) {
            credentials = getCredentials(pref.getTokenBasicAuth(), "basic");
        }
        return url+credentials;
    }

    private static String getCredentials(String token, String type) {
        if (token.isEmpty() || type.isEmpty())
            return "";
        return "token=" + token + "&type=" + type;
    }
}
