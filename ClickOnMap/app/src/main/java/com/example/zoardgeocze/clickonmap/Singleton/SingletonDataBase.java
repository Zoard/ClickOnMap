package com.example.zoardgeocze.clickonmap.Singleton;

import android.app.Activity;
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
                    "firebaseKey TEXT PRIMARY KEY" +
                    ");", // FIM CREATE TABLE

            "CREATE TABLE VGISystems (" +
                    "systemAdress TEXT PRIMARY KEY," +
                    "systemName TEXT NOT NULL," +
                    "userEmail TEXT NOT NULL," +
                    "dtCadastro TEXT NOT NULL," +
                    "numColaborations INTEGER," +
                    "color TEXT,"+
                    "hasSession TEXT NOT NULL" +
                    ");", // FIM CREATE TABLE

            "CREATE TABLE Users (" +
                    "userEmail TEXT NOT NULL," +
                    "systemAdress TEXT NOT NULL," +
                    "userName TEXT NOT NULL," +
                    "userPassword TEXT NOT NULL," +
                    "PRIMARY KEY  (userEmail,systemAdress)," +
                    "CONSTRAINT fk_User_key FOREIGN KEY (systemAdress) REFERENCES VGISystems (systemAdress)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE PendingNotifications (" +
                    "idNotifications INTEGER NOT NULL," +
                    "userEmail TEXT NOT NULL," +
                    "PRIMARY KEY  (idNotifications,userEmail)," +
                    "CONSTRAINT fk_PendingNotifications_key FOREIGN KEY (userEmail) REFERENCES Users (userEmail)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE Categories (" +
                    "idCategory INTEGER NOT NULL," +
                    "systemAdress TEXT NOT NULL," +
                    "categoryDescription TEXT NOT NULL," +
                    "PRIMARY KEY  (idCategory,systemAdress)," +
                    "CONSTRAINT fk_Categories_key FOREIGN KEY (systemAdress) REFERENCES VGISystems (systemAdress)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE CategoriesTypes (" +
                    "idCategoryType INTEGER NOT NULL," +
                    "idCategory INTEGER NOT NULL," +
                    "PRIMARY KEY  (idCategoryType,idCategory)," +
                    "CONSTRAINT fk_CategoriesTypes_key FOREIGN KEY (idCategory) REFERENCES Categories (idCategory)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE PendingColaborations (" +
                    "idColaborations INTEGER NOT NULL," +
                    "systemAdress TEXT NOT NULL," +
                    "idCategory INTEGER NOT NULL," +
                    "idCategoryType INTEGER NOT NULL," +
                    "PRIMARY KEY  (idColaborations,systemAdress,idCategory,idCategoryType)," +
                    "CONSTRAINT fk_PendingColaborations_key FOREIGN KEY (systemAdress) REFERENCES VGISystems (systemAdress)," +
                    "CONSTRAINT fk_PendingColaborations_key FOREIGN KEY (idCategory) REFERENCES Categories (idCategory)," +
                    "CONSTRAINT fk_PendingColaborations_key FOREIGN KEY (idCategoryType) REFERENCES CategoriesTypes (idCategoryType)" +
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
