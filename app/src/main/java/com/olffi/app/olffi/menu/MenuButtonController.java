package com.olffi.app.olffi.menu;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olffi.app.olffi.R;

/**
 * Created by gabrielmorin on 09/01/2017.
 */

public class MenuButtonController {
    View buttonView;
    Integer buttonViewId;
    ImageView imageView;
    TextView textView;
    Activity activity;
    String text;
    Integer imageResId;
    View.OnClickListener onClickListener;

    public MenuButtonController(Activity activity, Integer buttonViewId) {
        this.activity = activity;
        this.buttonViewId = buttonViewId;
        init();
    }

    private void init() {
        buttonView = activity.findViewById(buttonViewId);
        imageView = (ImageView) buttonView.findViewById(R.id.img);
        textView = (TextView) buttonView.findViewById(R.id.text);
    }

    public MenuButtonController setImageResId(Integer imageResId) {
        this.imageResId = imageResId;
        imageView.setImageDrawable(ContextCompat.getDrawable(activity, imageResId));
        imageView.setColorFilter(ContextCompat.getColor(activity, R.color.colorImageButton), PorterDuff.Mode.MULTIPLY);
        return this;
    }

    public MenuButtonController setText(String text) {
        this.text = text;
        textView.setText(text);
        return this;
    }

    public MenuButtonController setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        buttonView.setOnClickListener(onClickListener);
        return this;
    }

    public String getText() {
        return text;
    }
}
