package com.example.zoardgeocze.clickonmap.Design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ZoardGeocze on 27/04/17.
 */

public class AvenirBookTextView extends android.support.v7.widget.AppCompatTextView {
    public AvenirBookTextView(Context context) {
        super(context);
        init();
    }

    public AvenirBookTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirBookTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Book.otf");
        this.setTypeface(tf);
    }

}
