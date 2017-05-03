package com.example.zoardgeocze.clickonmap.Design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by ZoardGeocze on 27/04/17.
 */

public class AvenirLightTextView extends android.support.v7.widget.AppCompatTextView {

    public AvenirLightTextView(Context context) {
        super(context);
        init();
    }

    public AvenirLightTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirLightTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Light.otf");
        this.setTypeface(tf);
    }
}
