package com.example.zoardgeocze.clickonmap.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


/**
 * Created by ZoardGeocze on 02/10/2018.
 */

public class Alert {

    private AlertDialog.Builder builder;
    private String title;
    private String message;
    private String okButton;
    private String cancelButton;


    public Alert(Context context, String title, String message, String okButton, String cancelButton) {
        this.builder = new AlertDialog.Builder(context);
        this.title = title;
        this.message = message;
        this.okButton = okButton;
        this.cancelButton = cancelButton;
    }

    public boolean show() {

        final boolean[] button = {false};

        this.builder.setTitle(this.title);
        this.builder.setMessage(this.message);
        this.builder.setPositiveButton(this.okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button[0] = true;
            }
        });
        this.builder.setNegativeButton(this.cancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                button[0] = false;
            }
        });
        this.builder.create();
        this.builder.show();

        return button[0];
    }


}
