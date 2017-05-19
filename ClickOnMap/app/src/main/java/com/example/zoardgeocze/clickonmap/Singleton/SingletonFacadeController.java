package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.zoardgeocze.clickonmap.Model.SystemTile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public class SingletonFacadeController {

    private static SingletonFacadeController INSTANCE = new SingletonFacadeController();

    private List<SystemTile> menuTiles;

    private SingletonFacadeController() {
        this.daoMenuTiles();
    }

    public static SingletonFacadeController getInstance() {
        return INSTANCE;
    }

    //Implmentar os Tiles
    private void daoMenuTiles() {
        this.menuTiles = new ArrayList<>();
        SingletonDataBase db = SingletonDataBase.getInstance();
        //Cursor c = db.search("");
    }

    public boolean registerFirebaseKey(Context context, String firebaseKey) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("device",null);

        ContentValues newKey = new ContentValues();
        newKey.put("firebaseKey",firebaseKey);

        db.insert("device",newKey);

        db.close();

        return true;
    }

}
