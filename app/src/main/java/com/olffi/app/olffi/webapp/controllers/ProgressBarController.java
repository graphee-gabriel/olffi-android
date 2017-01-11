package com.olffi.app.olffi.webapp.controllers;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class ProgressBarController {
    private ProgressBar progressBarInApp;

    public ProgressBarController(ProgressBar progressBarInApp) {
        this.progressBarInApp = progressBarInApp;
    }

    public void showLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (progressBarInApp != null)
            progressBarInApp.setVisibility(View.GONE);
    }
}
