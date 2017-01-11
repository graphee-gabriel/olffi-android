package com.olffi.app.olffi.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by gabrielmorin on 11/01/2017.
 */

public class AppTypeFace {
    public static class FontUrl {
        public static final String
                REGULAR = "fonts/Arvo-Regular.ttf",
                BOLD = "fonts/Arvo-Bold.ttf";
    }

    public static void setTypeface(TextView textView) {
        setTypeface(textView, FontUrl.REGULAR, FontUrl.BOLD);
    }

    public static void setTypeface(TextView textView, AttributeSet attrs) {
        setTypeface(textView, attrs, FontUrl.REGULAR, FontUrl.BOLD);
    }

    public static void setTypeface(TextView textView, String fontUrlRegular, String fontUrlBold) {
        setTypeface(textView, null, fontUrlRegular, fontUrlBold);
    }

    public static void setTypeface(TextView textView, AttributeSet attrs, String fontUrlRegular, String fontUrlBold) {
        // Check if phone handles missing characters (Starting API LVL XX)
        // Else replace missing characters by space, this is not acceptable
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (textView != null) {
                int style = 0;
                if (textView.getTypeface() != null) {
                    style = textView.getTypeface().getStyle();
                } else if (attrs != null) {
                    style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", 0);
                }
                Context context = textView.getContext();
                if (context != null) {
                    AssetManager assetManager = textView.getContext().getAssets();
                    Typeface tf = Typeface.createFromAsset(assetManager,
                            style == Typeface.BOLD ?
                                    fontUrlBold : fontUrlRegular
                    );
                    textView.setTypeface(tf);
                }
            }
        }
    }

    public static void setTypeface(TextView textView, String fontUrl) {
        setTypeface(textView, fontUrl, fontUrl);
    }
}
