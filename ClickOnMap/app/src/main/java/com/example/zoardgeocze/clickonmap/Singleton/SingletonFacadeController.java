package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public class SingletonFacadeController {

    private static SingletonFacadeController INSTANCE = new SingletonFacadeController();

    public static SingletonFacadeController getInstance() {
        return INSTANCE;
    }

    public boolean registerFirebaseKey(String firebaseKey) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("device",null);

        ContentValues newKey = new ContentValues();
        newKey.put("firebaseKey",firebaseKey);

        db.insert("device",newKey);

        db.close();

        return true;
    }

}
