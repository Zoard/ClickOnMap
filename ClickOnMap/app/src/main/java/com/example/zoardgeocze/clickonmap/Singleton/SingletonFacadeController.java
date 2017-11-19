package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.MenuActivity;
import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.EventType;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import java.util.ArrayList;
import java.util.List;

import rx.Single;

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

        Cursor c = db.search("SystemVGI",new String[]{"address","name","color",
                            "collaborations","systemDescription","latX","latY","lngX","lngY"},"","");
        Log.d("Teste", "Cont tiles: " + c.getCount());

        while (c.moveToNext()) {

            VGISystem vgiSystem = new VGISystem();

            vgiSystem.setAddress(c.getString(c.getColumnIndex("address")));
            vgiSystem.setName(c.getString(c.getColumnIndex("name")));
            vgiSystem.setColor(c.getString(c.getColumnIndex("color")));
            vgiSystem.setCollaborations(c.getInt(c.getColumnIndex("collaborations")));
            vgiSystem.setDescription(c.getString(c.getColumnIndex("systemDescription")));
            vgiSystem.setLatX(c.getDouble(c.getColumnIndex("latX")));
            vgiSystem.setLatY(c.getDouble(c.getColumnIndex("latY")));
            vgiSystem.setLngX(c.getDouble(c.getColumnIndex("lngX")));
            vgiSystem.setLngY(c.getDouble(c.getColumnIndex("lngY")));

            getEventCategories(vgiSystem.getAddress());

            SystemTile systemTile = new SystemTile(vgiSystem);

            this.menuTiles.add(0,systemTile);

        }

        c.close();
    }

    private void getEventCategories(String address) {

    }

    public List<SystemTile> getMenuTiles() {
        return menuTiles;
    }

    private void registerSystemVGI(VGISystem vgiSystem, User user) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newSystemVGI = new ContentValues();

        newSystemVGI.put("address",vgiSystem.getAddress());
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

        Cursor c = db.search("SystemVGI",new String[]{"address"},"address = '" + vgiSystem.getAddress() + "'","");
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

        boolean verifySystem = false;

        if (searchVGISystem(vgiSystem)) {
            registerSystemVGI(vgiSystem,user);
            verifySystem = true;
        }

        ContentValues newUser = new ContentValues();

        newUser.put("userId",user.getId());
        newUser.put("systemAddress",vgiSystem.getAddress());
        newUser.put("name",user.getName());
        newUser.put("password",user.getPassword());
        newUser.put("email",user.getEmail());
        newUser.put("registerDate",user.getRegisterDate());

        db.insert("User",newUser);

        if(!verifySystem) {
            String hasSession = "Y";

            ContentValues contentValues = new ContentValues();
            contentValues.put("userId",user.getId());
            contentValues.put("hasSession",hasSession);

            db.update("SystemVGI",contentValues,"address = '" + vgiSystem.getAddress() + "'");
        }

        return verifySystem;
    }

    public boolean updateVGISystemAddress(String oldAddress, String newAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues updateSystem = new ContentValues();

        updateSystem.put("address",newAddress);

        db.update("SystemVGI",updateSystem,"address = '" + oldAddress + "'");

        return true;
    }

    public boolean deleteVGISystem(String address) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("SystemVGI","address = '" + address + "'");

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

    public String hasSession(String address) {

        SingletonDataBase db = SingletonDataBase.getInstance();
        Cursor c = db.search("SystemVGI", new String[]{"hasSession"},"address = '" + address + "'","");
        String systemAddress = "N";

        while (c.moveToNext()) {
            systemAddress = c.getString(c.getColumnIndex("hasSession"));
        }

        c.close();

        return systemAddress;

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
                        "address = '" + vgiSystem.getAddress() + "' AND userId = '" + user.getId() + "'");

                return true;

            }
            //Se usuário não estiver na tabela VGISystem, mas for um usuário do sistema, registra-o na tabela VGISystem e marca 'Y' para sessão
            else if(verifyUserInUserTable(user,vgiSystem)) {

                String hasSession = "Y";

                ContentValues contentValues = new ContentValues();
                contentValues.put("userId",user.getId());
                contentValues.put("hasSession",hasSession);

                db.update("SystemVGI",contentValues,"address = '" + vgiSystem.getAddress() + "'");

                return true;

            }
            //Caso contrário, registra usuário no sistema
            else {
                registerUser(vgiSystem, user);

                return true;
            }

        } else {
            return false;
        }

    }

    public void vgiSystemLogout(String systemAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues contentValues = new ContentValues();

        contentValues.put("hasSession","N");

        db.update("SystemVGI",contentValues,"address = '" + systemAddress + "'");

    }

    public boolean verifyUserInSystemTable(User user, VGISystem vgiSystem) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI",new String[]{"userId"},
                "userId = '" + user.getId() + "' AND address = '" + vgiSystem.getAddress() + "'","");

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
                "email = '" + user.getEmail() + "' AND systemAddress = '" + vgiSystem.getAddress() + "'","");

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
        String userId = getUserId(vgiSystem.getAddress());

        Cursor c = db.search("User", new String[]{"userId","name","password","email","registerDate"},
                "userId = '" + userId + "' AND systemAddress = '" + vgiSystem.getAddress() + "'","");

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

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getUserId(String systemAddress) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("SystemVGI", new String[]{"userId"},"address = '" + systemAddress + "' AND hasSession = 'Y'","");

        String userId = "";

        while(c.moveToNext()) {
            userId = c.getString(c.getColumnIndex("userId"));
        }

        return userId;
    }

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getUserName(String userId) {
        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("User", new String[]{"name"},"userId = '" + userId + "'","");

        String userName = "";

        while(c.moveToNext()) {
            userName = c.getString(c.getColumnIndex("name"));
        }

        return userName;
    }

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public boolean registerPendingCollaborations(Collaboration collaboration, String systemAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ContentValues newCollaboration = new ContentValues();

        newCollaboration.put("eventCategory_categoryId",collaboration.getCategoryId());
        newCollaboration.put("eventCategory_categoryName",collaboration.getCategoryName());
        newCollaboration.put("eventType_typeId",collaboration.getSubcategoryId());
        newCollaboration.put("eventType_typeName",collaboration.getSubcategoryName());
        newCollaboration.put("user_userId",collaboration.getUserId());
        newCollaboration.put("user_systemAddress", systemAddress);
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

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public List<Collaboration> getCollaborations(String systemAddress) {

        List<Collaboration> collaborationList = new ArrayList<>();

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",
                new String[] {"eventCategory_categoryId", "eventCategory_categoryName", "eventType_typeId",
                              "eventType_typeName","user_userId", "title","description","collaborationDate",
                              "picture","video","latitude","longitude"},
                "user_systemAddress = '" + systemAddress + "'","");

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

    //TODO: VERIFICAR SE A DELEÇÃO DA TABELA PODE CAUSAR PROBLEMAS PARA COLABORAÇÕES PENDENTES
    //Função que registra Categorias e Tipos de Eventos
    public void registerCategory(VGISystem vgiSystem) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        db.delete("EventCategory","");

        for (EventCategory category:vgiSystem.getCategory()) {

            ContentValues newCategory = new ContentValues();

            newCategory.put("serverAddress",vgiSystem.getAddress());
            newCategory.put("eventCategoryId", category.getId());
            newCategory.put("eventCategoryDescription",category.getDescription());

            long categoryId = db.insert("EventCategory",newCategory);

            for (EventType type:category.getEventTypes()) {

                ContentValues newType = new ContentValues();

                newType.put("categoryId",categoryId);
                newType.put("eventTypeId", type.getId());
                newType.put("eventTypeDescription",type.getDescription());

                db.insert("EventType",newType);
            }

        }

    }



    //TODO GEOINFO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO
    public ArrayList<String> getCategoriesFromSystem(String systemAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        ArrayList<String> categories = new ArrayList<>();

        Cursor c = db.search("EventCategory",new String[]{"categoryId","eventCategoryDescription"},
                            "serverAddress = '" + systemAddress + "'", "categoryId ASC");

        while(c.moveToNext()) {
            int categoryId = c.getInt(c.getColumnIndex("categoryId"));
            String categoryName = c.getString(c.getColumnIndex("eventCategoryDescription"));
            categories.add(categoryName);
        }

        return categories;

    }

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public ArrayList<String> getTypesFromSystem(int categoryId) {

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

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getLastCollaboration(String systemAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",new String[] {"collaborationDate"},
                "user_systemAddress = '" + systemAddress + "'","collaborationsId DESC");

        String lastColab = "";

        if(c.getCount() > 0) {
            c.moveToFirst();

            lastColab = c.getString(c.getColumnIndex("collaborationDate"));
        }

        return lastColab;

    }

    //TODO:FUNÇÃO TEMPORÁRIA - CRIADA PARA Demo DO GEOINFO
    public String getMostCollaborator(String systemAddress) {

        SingletonDataBase db = SingletonDataBase.getInstance();

        Cursor c = db.search("PendingCollaborations",new String[] {"user_userId","COUNT(user_userId) AS userId"},
                "user_systemAddress = '" + systemAddress + "'","userId DESC");

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
