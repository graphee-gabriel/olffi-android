package com.olffi.app.olffi.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.olffi.app.olffi.tools.AppTypeFace;

/**
 * Created by gabrielmorin on 25/09/2016.
 */

public class TextViewApp extends TextView {

    public TextViewApp(Context context) {
        super(context);
        AppTypeFace.setTypeface(this);
    }

    public TextViewApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        AppTypeFace.setTypeface(this, attrs);
    }

    public TextViewApp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AppTypeFace.setTypeface(this, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextViewApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        AppTypeFace.setTypeface(this, attrs);
    }
}
