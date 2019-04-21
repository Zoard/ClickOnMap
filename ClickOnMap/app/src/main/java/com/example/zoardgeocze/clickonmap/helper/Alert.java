package com.example.zoardgeocze.clickonmap.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


/**
 * Created by ZoardGeocze on 02/10/2018.
 */

public class Alert extends AlertDialog.Builder {

    public Alert(Context context, String title, String message) {
        super(context);
        this.setTitle(title);
        this.setMessage(message);
    }

    public Alert(@NonNull Context context) {
        super(context);
    }

    public void showDialog() {
        this.create();
        this.show();
    }


}
