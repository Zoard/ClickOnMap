package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.MenuActivity;
import com.example.zoardgeocze.clickonmap.Model.Collaboration;
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


    public boolean registerUser(VGISystem vgiSystem, User user) {

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

    //Função que verifica se o sistema está no Hub e faz login do Usuário
    public boolean vgiSystemLogin(VGISystem vgiSystem, User user) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        //Verifica se o Sistema está no Hub
        if(!searchVGISystem(vgiSystem)) {

            //Verifica se usuário está na Tabela VGISystem, se estiver, basta atualizar a tabela marcando 'Y' na sessão
            if(verifyUserInSystemTable(user,vgiSystem)) {

                String hasSession = "Y";

                ContentValues contentValues = new ContentValues();
                contentValues.put("hasSession",hasSession);
                db.update("SystemVGI",
                        contentValues,
                        "adress = '" + vgiSystem.getAdress() + "' AND userId = '" + user.getId() + "'");

                return true;

            }
            //Se usuário não estiver na tabela VGISystem, mas for um usuário do sistema, registra-o na tabela VGISystem e marca 'Y' para sessão
            else if(verifyUserInUserTable(user,vgiSystem)) {

                String hasSession = "Y";

                ContentValues contentValues = new ContentValues();
                contentValues.put("userId",user.getId());
                contentValues.put("hasSession",hasSession);

                db.update("SystemVGI",contentValues,"adress = '" + vgiSystem.getAdress() + "'");

                return true;

            }
            //Caso contrário, registra usuário no sistema
            else {
                registerUser(vgiSystem, user);

                String hasSession = "Y";

                ContentValues contentValues = new ContentValues();
                contentValues.put("userId",user.getId());
                contentValues.put("hasSession",hasSession);

                db.update("SystemVGI",contentValues,"adress = '" + vgiSystem.getAdress() + "'");

                return true;
            }

        } else {
            return false;
        }

    }

    public void vgiSystemLogout(String systemAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues contentValues = new ContentValues();

        contentValues.put("hasSession","N");

        db.update("SystemVGI",contentValues,"adress = '" + systemAdress + "'");

    }

    public boolean verifyUserInSystemTable(User user, VGISystem vgiSystem) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI",new String[]{"userId"},
                "userId = '" + user.getId() + "' AND adress = '" + vgiSystem.getAdress() + "'","");

        if(c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }

    }

    public boolean verifyUserInUserTable(User user, VGISystem vgiSystem) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("User",new String[]{"email"},
                "email = '" + user.getEmail() + "' AND systemAdress = '" + vgiSystem.getAdress() + "'","");

        if(c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    //TODO: Mudar depois o id do usuário para um inteiro ou UUID
    public User getUser(VGISystem vgiSystem) {

        SingletonDataBase db = SingletonDataBase.getInstance();
        String userId = getUserId(vgiSystem.getAdress());

        Cursor c = db.search("User", new String[]{"userId","name","password","email","registerDate"},
                "userId = '" + userId + "' AND systemAdress = '" + vgiSystem.getAdress() + "'","");

        User user = new User();

        while(c.moveToNext()) {
            user.setId(c.getString(c.getColumnIndex("userId")));
            user.setName(c.getString(c.getColumnIndex("name")));
            user.setPassword(c.getString(c.getColumnIndex("password")));
            user.setEmail(c.getString(c.getColumnIndex("email")));
            user.setRegisterDate(c.getString(c.getColumnIndex("registerDate")));
        }

        c.close();

        return user;

    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getUserId(String systemAdress) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI", new String[]{"userId"},"adress = '" + systemAdress + "' AND hasSession = 'Y'","");

        String userId = "";

        while(c.moveToNext()) {
            userId = c.getString(c.getColumnIndex("userId"));
        }

        return userId;
    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getUserName(String userId) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("User", new String[]{"name"},"userId = '" + userId + "'","");

        String userName = "";

        while(c.moveToNext()) {
            userName = c.getString(c.getColumnIndex("name"));
        }

        return userName;
    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public boolean registerPendingCollaborations(Collaboration collaboration, String systemAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newCollaboration = new ContentValues();

        newCollaboration.put("eventCategory_categoryId",collaboration.getCategoryId());
        newCollaboration.put("eventCategory_categoryName",collaboration.getCategoryName());
        newCollaboration.put("eventType_typeId",collaboration.getSubcategoryId());
        newCollaboration.put("eventType_typeName",collaboration.getSubcategoryName());
        newCollaboration.put("user_userId",collaboration.getUserId());
        newCollaboration.put("user_systemAdress", systemAdress);
        newCollaboration.put("title",collaboration.getTitle());
        newCollaboration.put("description",collaboration.getDescription());
        newCollaboration.put("collaborationDate",collaboration.getCollaborationDate());
        newCollaboration.put("picture",collaboration.getPhoto());
        newCollaboration.put("video",collaboration.getVideo());
        newCollaboration.put("latitude",collaboration.getLatitude());
        newCollaboration.put("longitude",collaboration.getLongitude());

        db.insert("PendingCollaborations",newCollaboration);

        return true;
    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public List<Collaboration> getCollaborations(String systemAdress) {

        List<Collaboration> collaborationList = new ArrayList<>();

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",
                new String[] {"eventCategory_categoryId", "eventCategory_categoryName", "eventType_typeId",
                              "eventType_typeName","user_userId", "title","description","collaborationDate",
                              "picture","video","latitude","longitude"},
                "user_systemAdress = '" + systemAdress + "'","");

        Collaboration collaboration;

        while(c.moveToNext()) {

            int categoryId = c.getInt(c.getColumnIndex("eventCategory_categoryId"));
            String categoryName = c.getString(c.getColumnIndex("eventCategory_categoryName"));
            int subcategoryId = c.getInt(c.getColumnIndex("eventType_typeId"));
            String subcategoryName = c.getString(c.getColumnIndex("eventType_typeName"));
            String userId = c.getString(c.getColumnIndex("user_userId"));
            String title = c.getString(c.getColumnIndex("title"));
            String description = c.getString(c.getColumnIndex("description"));
            String date = c.getString(c.getColumnIndex("collaborationDate"));
            String photoPath = c.getString(c.getColumnIndex("picture"));
            String videoPath = c.getString(c.getColumnIndex("video"));
            Double lat = c.getDouble(c.getColumnIndex("latitude"));
            Double lng = c.getDouble(c.getColumnIndex("longitude"));

            collaboration = new Collaboration(userId,title,description,date,categoryId,
                            categoryName,subcategoryId,subcategoryName,photoPath,videoPath,"",lat,lng);

            collaborationList.add(collaboration);

        }

        return collaborationList;

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

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getLastCollaboration(String systemAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",new String[] {"collaborationDate"},
                "user_systemAdress = '" + systemAdress + "'","collaborationsId DESC");

        String lastColab = "";

        if(c.getCount() > 0) {
            c.moveToFirst();

            lastColab = c.getString(c.getColumnIndex("collaborationDate"));
        }

        return lastColab;

    }

    //FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getMostCollaborator(String systemAdress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",new String[] {"user_userId","COUNT(user_userId) AS userId"},
                "user_systemAdress = '" + systemAdress + "'","userId DESC");

        String mostColab = "";

        if(c.getCount() > 0) {
            c.moveToFirst();

            mostColab = getUserName(c.getString(c.getColumnIndex("user_userId")));
        }

        return mostColab;
    }


    //Atribui valor NULO ao objeto para que o mesmo seja criado na próxima entrada
    //Attributes NULL value to the SingletonFacadeController object
    public void closeSingleton(){
        INSTANCE = null;
        Log.d("Teste", "Matei singleton");
    }


}
