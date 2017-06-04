package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

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

    //Implementar os Tiles
    private void daoMenuTiles() {
        this.menuTiles = new ArrayList<>();
        SingletonDataBase db = SingletonDataBase.getInstance();
        //Cursor c = db.search("");
    }

    public boolean registerUserSystem(Context context, VGISystem vgiSystem, User user) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newUserSystem = new ContentValues();

        newUserSystem.put("systemAdress",vgiSystem.getAdress());
        newUserSystem.put("systemName",vgiSystem.getName());
        newUserSystem.put("userLogin",user.getName());
        newUserSystem.put("dtCadastro",user.getRegisterDate());
        newUserSystem.put("hasSession","Y");

        db.insert("userSystem",newUserSystem);

        //db.close();

        return true;
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

    public boolean registerDeviceSystem(Context context, VGISystem vgiSystem) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("device", new String[] {"firebaseKey"},"","firebaseKey");

        ContentValues newDeviceSystem = new ContentValues();

        while(c.moveToNext()) {
            int key = c.getColumnIndex("firebaseKey");
            String firebaseKey = c.getString(key);
            newDeviceSystem.put("deviceKey",firebaseKey);
            newDeviceSystem.put("systemAdress",vgiSystem.getAdress());
        }

        c.close();

        db.insert("deviceSystem",newDeviceSystem);

        //db.close();

        return true;
    }

}
