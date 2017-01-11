package com.olffi.app.olffi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.olffi.app.olffi.controllers.ToolbarController;
import com.olffi.app.olffi.data.App;

import static android.view.View.VISIBLE;

public class LoadUrlWebAppActivity extends WebAppActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String url = extras.getString(App.INTENT_EXTRA_URL);
        String title = extras.getString(App.INTENT_EXTRA_TITLE);
        ToolbarController toolbarController = new ToolbarController(this);
        toolbarController.setVisibility(VISIBLE);
        toolbarController.setShowBackButton(true);

        setTitle(title);
        loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
