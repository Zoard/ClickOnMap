package com.example.zoardgeocze.clickonmap.Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public class MyApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }

}
