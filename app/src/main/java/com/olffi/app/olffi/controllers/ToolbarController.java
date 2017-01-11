package com.olffi.app.olffi.controllers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.olffi.app.olffi.R;

/**
 * Created by gabrielmorin on 09/01/2017.
 */

public class ToolbarController {
    private Toolbar toolbar;
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private boolean showBackButton = false;
    private boolean showTitle = true;

    public ToolbarController(AppCompatActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (toolbar != null) {
            activity.setSupportActionBar(toolbar);
            actionBar = activity.getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(showBackButton);
            actionBar.setDisplayShowHomeEnabled(showBackButton);
            actionBar.setDisplayShowTitleEnabled(showTitle);
            actionBar.setElevation(activity.getResources().getDimension(R.dimen.toolbar_elevation));
        }
    }

    public void setVisibility(int visibility) {
        if (toolbar != null)
            toolbar.setVisibility(visibility);
    }

    public void setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        actionBar.setDisplayHomeAsUpEnabled(showBackButton);
        actionBar.setDisplayShowHomeEnabled(showBackButton);
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
        actionBar.setDisplayShowTitleEnabled(showTitle);
    }
}
