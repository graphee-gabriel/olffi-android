package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.olffi.app.olffi.controllers.ToolbarController;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;
import com.olffi.app.olffi.menu.MenuController;
import com.olffi.app.olffi.notifications.NotificationRegistrationManager;
import com.olffi.app.olffi.webapp.UrlBuilder;

public class MenuActivity extends AppCompatActivity implements MenuController.MenuNavigationListener {

    private final static String TAG = MenuActivity.class.getSimpleName();

    private MenuController menuController;
    private ToolbarController toolbarController;
    private NotificationRegistrationManager notificationRegistrationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbarController = new ToolbarController(this);
        toolbarController.setShowTitle(false);
        menuController = new MenuController(this);
        menuController.setListener(this);
        notificationRegistrationManager = new NotificationRegistrationManager(this);
        notificationRegistrationManager.startIntentRegistration();
        findViewById(R.id.button_settings).setOnClickListener(this::onClickSettings);
    }


    @Override
    protected void onResume() {
        super.onResume();
        notificationRegistrationManager.registerReceiver();
    }

    @Override
    protected void onPause() {
        notificationRegistrationManager.unRegisterReceiver();
        super.onPause();
    }

    @Override
    public void onClickSearch(View view) {
        App.startSearch(this);
    }

    @Override
    public void onClickCountryRegulations(View view) {
        App.startCountryList(this);
    }

    @Override
    public void onClickCoproductionTreaties(View view) {
        Log.e(TAG, "onClickCoproductionTreaties: ");
    }

    @Override
    public void onClickCalendar(View view) {
        startWebAppFromRelativeUrl(
                "/program/calendar.html",
                getString(R.string.title_calendar)
        );
    }

    @Override
    public void onClickPublications(View view) {
        startWebAppFromRelativeUrl(
                "/publication.html",
                getString(R.string.title_publication)
        );
    }

    @Override
    public void onClickCompare(View view) {
        startWebAppFromRelativeUrl(
                "/program/compare.html",
                getString(R.string.title_compare)
        );
    }

    private void onClickSettings(View view) {
        startWebAppFromRelativeUrl(
                "/account.html",
                getString(R.string.title_settings)
        );
    }

    private void startWebAppFromRelativeUrl(String relativeUrl, String title) {
        App.startWebAppFromUrl(this,
                UrlBuilder.getUrlFromRelativeUrl(new UserPreferences(this), relativeUrl),
                title);
    }
}
