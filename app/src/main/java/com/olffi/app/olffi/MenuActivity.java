package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.olffi.app.olffi.controllers.ToolbarController;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.UserPreferences;
import com.olffi.app.olffi.menu.MenuController;
import com.olffi.app.olffi.webapp.UrlBuilder;

public class MenuActivity extends AppCompatActivity implements MenuController.MenuNavigationListener {

    private final static String TAG = MenuActivity.class.getSimpleName();

    MenuController menuController;
    ToolbarController toolbarController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbarController = new ToolbarController(this);
        toolbarController.setShowTitle(false);
        menuController = new MenuController(this);
        menuController.setListener(this);
        findViewById(R.id.button_settings).setOnClickListener(this::onClickSettings);
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
                "Funding Calendar"
        );
    }

    @Override
    public void onClickPublications(View view) {
        startWebAppFromRelativeUrl(
                "/publication.html",
                "Publications"
        );
    }

    @Override
    public void onClickCompare(View view) {
        startWebAppFromRelativeUrl(
                "/program/compare.html",
                "Compare Programs"
        );
    }

    private void onClickSettings(View view) {
        startWebAppFromRelativeUrl(
                "/account.html",
                "Settings"
        );
    }

    private void startWebAppFromRelativeUrl(String relativeUrl, String title) {
        App.startWebAppFromUrl(this,
                UrlBuilder.getUrlFromRelativeUrl(new UserPreferences(this), relativeUrl),
                title);
    }
}
