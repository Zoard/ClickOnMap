package com.example.zoardgeocze.clickonmap.Design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class AvenirBookEditText extends android.support.v7.widget.AppCompatEditText {

    public AvenirBookEditText(Context context) {
        super(context);
        init();
    }

    public AvenirBookEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvenirBookEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirLTStd-Book.otf");
        this.setTypeface(tf);
    }


}
