package com.olffi.app.olffi.webapp.controllers;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by gabrielmorin on 05/01/2017.
 */

public class ProgressBarController {
    private ProgressBar progressBarInApp;

    public ProgressBarController(ProgressBar progressBarInApp) {
        this.progressBarInApp = progressBarInApp;

        if (progressBarInApp != null) {
            Resources r = progressBarInApp.getContext().getResources();
            progressBarInApp.getIndeterminateDrawable().setColorFilter(r.getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
            progressBarInApp.getProgressDrawable().setColorFilter(r.getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        }
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
