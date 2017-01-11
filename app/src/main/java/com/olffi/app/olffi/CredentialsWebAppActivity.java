package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.olffi.app.olffi.data.UserPreferences;
import com.olffi.app.olffi.webapp.UrlBuilder;

public class CredentialsWebAppActivity extends WebAppActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String url = UrlBuilder.getUrlWebAppWithCredentials(new UserPreferences(this));
        loadUrl(url);
    }
}
