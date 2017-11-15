package com.example.zoardgeocze.clickonmap.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Util.MyApp;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public class SingletonDataBase {

    private final String DB_NAME = "clickonmap_db";

    private static SingletonDataBase INSTANCE = new SingletonDataBase();

    private final String[] SCRIPT_DATA_BASE_CREATE = new String[]{
            "CREATE TABLE Device (" +
                    "fcmKey TEXT NOT NULL," +
                    "creationDate TEXT NOT NULL," +
                    "PRIMARY KEY (fcmKey)" +
                    ");", // FIM CREATE TABLE

            "CREATE TABLE SystemVGI (" +
                    "address TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "color TEXT," +
                    "collaborations INTEGER," +
                    "userId TEXT NOT NULL," + //GEOINFO - MUDEI DE INTEGER PARA TEXT
                    "latX REAL NOT NULL," +
                    "latY REAL NOT NULL," +
                    "lngX REAL NOT NULL," +
                    "lngY REAL NOT NULL," +
                    "hasSession TEXT NOT NULL,"+
                    "systemDescription TEXT NOT NULL," +
                    "PRIMARY KEY (address)" +
                    ");", // FIM CREATE TABLE

            "CREATE TABLE User (" +
                    "userId TEXT NOT NULL," + //GEOINFO - MUDEI DE INTEGER PARA TEXT
                    "systemAddress TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "registerDate TEXT NOT NULL," +
                    "PRIMARY KEY (userId, systemAddress)," +
                    "CONSTRAINT systemAddress FOREIGN KEY (systemAddress) REFERENCES SystemVGI (address) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT candidateKeyUser UNIQUE (systemAddress, email)" + //MUDEI AQUI "name" para "email", NÃO FAZ SENTIDO SER "name"
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE Notification (" +
                    "notificationId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId TEXT NOT NULL," + //GEOINFO - MUDEI DE INTEGER PARA TEXT
                    "userSystemAddress TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "CONSTRAINT user_userId_userSystemAddress FOREIGN KEY (userId, userSystemAddress) REFERENCES User (userId, systemAddress) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE EventCategory (" +
                    "categoryId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "serverAddress TEXT NOT NULL," +
                    "eventCategoryId INTEGER NOT NULL," +
                    "eventCategoryDescription TEXT," +
                    "CONSTRAINT serverAddress FOREIGN KEY (serverAddress) REFERENCES SystemVGI (address) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT candidateKeyEventCategory UNIQUE (serverAddress, eventCategoryId)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE EventType (" +
                    "typeId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "categoryId INTEGER NOT NULL," +
                    "eventTypeId INTEGER NOT NULL," +
                    "eventTypeDescription TEXT," +
                    "CONSTRAINT categoryId FOREIGN KEY (categoryId) REFERENCES EventCategory (categoryId) ON DELETE CASCADE ON UPDATE CASCADE," +
                    "CONSTRAINT candidateKeyEventType UNIQUE (categoryId, eventTypeId)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE PendingCollaborations (" +
                    "collaborationsId INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "eventCategory_categoryId INTEGER NOT NULL," +
                    "eventCategory_categoryName TEXT," + //Adicionado pro GEOINFO
                    "eventType_typeId INTEGER NOT NULL," +
                    "eventType_typeName TEXT," + //Adicionado pro GEOINFO
                    "user_userId TEXT NOT NULL," + //GEOINFO - MUDEI DE INTEGER PARA TEXT
                    "user_systemAddress TEXT NOT NULL," +
                    "title TEXT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "collaborationDate TEXT NOT NULL," +
                    "picture TEXT," +
                    "video TEXT," +
                    "latitude REAL NOT NULL," +
                    "longitude REAL NOT NULL," +
                    "CONSTRAINT eventCategory_categoryId FOREIGN KEY (eventCategory_categoryId) REFERENCES EventCategory (categoryId) ON DELETE NO ACTION ON UPDATE NO ACTION," +
                    "CONSTRAINT eventType_typeId FOREIGN KEY (eventType_typeId) REFERENCES EventType (typeId) ON DELETE NO ACTION ON UPDATE NO ACTION," +
                    "CONSTRAINT user_userId_systemAddress FOREIGN KEY (user_userId, user_systemAddress) REFERENCES User (userId, systemAddress) ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");" // FIM CREATE TABLE*/

    };

    protected SQLiteDatabase db;

    private SingletonDataBase() {
        this.open(MyApp.getAppContext());

        Cursor c = this.search("sqlite_master", null, "type = 'table'", "");

        //Cria tabelas do banco de dados caso o mesmo estiver vazio
        if(c.getCount() == 1){
            for(int i = 0; i < this.SCRIPT_DATA_BASE_CREATE.length; i++){
                this.db.execSQL(this.SCRIPT_DATA_BASE_CREATE[i]);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
        }

        c.close();
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    public static SingletonDataBase getInstance(){
        return INSTANCE;
    }

    public long insert(String tabela, ContentValues valores) {
        long id = this.db.insert(tabela, null, valores);
        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    public int update(String tabela, ContentValues valores, String where) {
        int count = this.db.update(tabela, valores, where, null);
        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    public int delete(String tabela, String where) {
        int count = this.db.delete(tabela, where, null);
        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    public Cursor search(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;

        if(where.equals("")) {
            c = this.db.query(tabela, colunas, null, null, null, null, orderBy);
        } else {
            c = this.db.query(tabela, colunas, where, null, null, null, orderBy);
        }

        Log.i("BANCO_DADOS", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    public void open(Context ctx) {
        this.db = ctx.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    public void close() {
        if (this.db != null) {
            this.db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }

}
