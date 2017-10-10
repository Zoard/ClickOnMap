package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.MenuActivity;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public final class SingletonFacadeController {

    private static SingletonFacadeController INSTANCE;

    private List<SystemTile> menuTiles;

    private SingletonFacadeController() {
        Log.d("Teste", "Criou Controlador Fachada");
        this.daoMenuTiles();
    }

    public static SingletonFacadeController getInstance() {
        if(INSTANCE == null)
           INSTANCE = new SingletonFacadeController();
        return INSTANCE;
    }

    //TODO: Implementar os Tiles
    //TODO: Colocar as Categorias aqui quando o sistema carregar
    private void daoMenuTiles() {
        this.menuTiles = new ArrayList<>();

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI",new String[]{"adress","name","color",
                            "collaborations","systemDescription","latX","latY","lngX","lngY"},"","");
        Log.d("Teste", "Cont tiles: " + c.getCount());

        while (c.moveToNext()) {

            VGISystem vgiSystem = new VGISystem();

            vgiSystem.setAdress(c.getString(c.getColumnIndex("adress")));
            vgiSystem.setName(c.getString(c.getColumnIndex("name")));
            vgiSystem.setColor(c.getString(c.getColumnIndex("color")));
            vgiSystem.setCollaborations(c.getInt(c.getColumnIndex("collaborations")));
            vgiSystem.setDescription(c.getString(c.getColumnIndex("systemDescription")));
            vgiSystem.setLatX(c.getDouble(c.getColumnIndex("latX")));
            vgiSystem.setLatY(c.getDouble(c.getColumnIndex("latY")));
            vgiSystem.setLngX(c.getDouble(c.getColumnIndex("lngX")));
            vgiSystem.setLngY(c.getDouble(c.getColumnIndex("lngY")));

            SystemTile systemTile = new SystemTile(vgiSystem);

            this.menuTiles.add(0,systemTile);

        }

        c.close();
    }

    public List<SystemTile> getMenuTiles() {
        return menuTiles;
    }

    private void registerSystemVGI(VGISystem vgiSystem, User user) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newSystemVGI = new ContentValues();

        newSystemVGI.put("adress",vgiSystem.getAdress());
        newSystemVGI.put("name",vgiSystem.getName());
        newSystemVGI.put("color",vgiSystem.getColor());
        newSystemVGI.put("collaborations",vgiSystem.getCollaborations());
        newSystemVGI.put("userId",user.getId());
        newSystemVGI.put("latX",vgiSystem.getLatX());
        newSystemVGI.put("latY",vgiSystem.getLatY());
        newSystemVGI.put("lngX",vgiSystem.getLngX());
        newSystemVGI.put("lngY",vgiSystem.getLngY());
        newSystemVGI.put("hasSession","Y");
        newSystemVGI.put("systemDescription",vgiSystem.getDescription());

        db.insert("SystemVGI",newSystemVGI);

    }

    public boolean searchVGISystem(VGISystem vgiSystem) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI",new String[]{"adress"},"adress = '" + vgiSystem.getAdress() + "'","");
        if (!(c.getCount() > 0)) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }


    public boolean registerUser(Context context, VGISystem vgiSystem, User user) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        if (searchVGISystem(vgiSystem)) {
            registerSystemVGI(vgiSystem,user);
        }

        ContentValues newUser = new ContentValues();

        newUser.put("userId",user.getId());
        newUser.put("systemAdress",vgiSystem.getAdress());
        newUser.put("name",user.getName());
        newUser.put("password",user.getPassword());
        newUser.put("email",user.getEmail());
        newUser.put("registerDate",user.getRegisterDate());

        db.insert("User",newUser);

        return true;
    }

    public boolean updateVGISystemAdress(String oldAdress, String newAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues updateSystem = new ContentValues();

        updateSystem.put("adress",newAdress);

        db.update("SystemVGI",updateSystem,"adress = '" + oldAdress + "'");

        return true;
    }

    public boolean deleteVGISystem(String adress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("SystemVGI","adress = '" + adress + "'");

        return true;
    }

    //Registra a chave fcm quando o usuário instala o aplicativo
    //Register the fcm Key when the user installs the app
    public boolean registerFirebaseKey(Context context, String firebaseKey, String creationDate) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("Device",null);

        ContentValues newKey = new ContentValues();
        newKey.put("fcmKey",firebaseKey);
        newKey.put("creationDate",creationDate);

        db.insert("Device",newKey);

        return true;
    }

    public String getFirebaseKey() {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("Device", new String[]{"fcmKey"}, "", "");
        String firebaseKey = "";

        while (c.moveToNext()) {
            firebaseKey = c.getString(c.getColumnIndex("fcmKey"));
        }

        c.close();

        return firebaseKey;
    }

    public String hasSession(String adress) {

        SingletonDataBase db = SingletonDataBase.getInstance();
        Cursor c = db.search("SystemVGI", new String[]{"hasSession"},"adress = '" + adress + "'","");
        String systemAdress = "N";

        while (c.moveToNext()) {
            systemAdress = c.getString(c.getColumnIndex("hasSession"));
        }

        c.close();

        return systemAdress;

    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public boolean registerCategory(VGISystem vgiSystem, String[] subcategorias) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newCategory = new ContentValues();

        int eventCategoryID = 1;

        newCategory.put("serverAdress",vgiSystem.getAdress());
        newCategory.put("eventCategoryId", eventCategoryID);
        newCategory.put("eventCategoryDescription","Teste");

        db.insert("EventCategory",newCategory);

        ContentValues newSubcategory;

        for(int i = 0; i < subcategorias.length; i++) {

            newSubcategory = new ContentValues();
            newSubcategory.put("categoryId",eventCategoryID);
            newSubcategory.put("eventTypeId",i+1);
            newSubcategory.put("eventTypeDescription",subcategorias[i]);

            db.insert("EventType",newSubcategory);
        }

        return true;

    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public ArrayList<String> getCategoriesFromSystem(String systemAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ArrayList<String> categories = new ArrayList<>();

        Cursor c = db.search("EventCategory",new String[]{"categoryId","eventCategoryDescription"},
                            "serverAdress = '" + systemAdress + "'", "categoryId ASC");

        while(c.moveToNext()) {
            int categoryId = c.getInt(c.getColumnIndex("categoryId"));
            String categoryName = c.getString(c.getColumnIndex("eventCategoryDescription"));
            categories.add(categoryName);
        }

        return categories;

    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public ArrayList<String> getSubcategoriesFromSystem(int categoryId) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ArrayList<String> subcategories = new ArrayList<>();

        Cursor c = db.search("EventType",new String[]{"typeId","eventTypeDescription"},
                "categoryId = '" + categoryId + "'", "typeId ASC");

        while(c.moveToNext()) {

            int typeId = c.getInt(c.getColumnIndex("typeId"));
            String subcategoryName = c.getString(c.getColumnIndex("eventTypeDescription"));
            subcategories.add(subcategoryName);
            Log.i("SUBCATEGORIES: ", String.valueOf(typeId) + " " + subcategoryName);

        }

        return subcategories;

    }


    //Atribui valor NULO ao objeto para que o mesmo seja criado na próxima entrada
    //Attributes NULL value to the SingletonFacadeController object
    public void closeSingleton(){
        INSTANCE = null;
        Log.d("Teste", "Matei singleton");
    }


}
