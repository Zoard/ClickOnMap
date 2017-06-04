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
            "CREATE TABLE device (" +
                    "firebaseKey TEXT PRIMARY KEY" +
                    ");", // FIM CREATE TABLE

            "CREATE TABLE deviceSystem (" +
                    "deviceKey TEXT NOT NULL," +
                    "systemAdress TEXT NOT NULL," +
                    "PRIMARY KEY  (deviceKey,systemAdress)," +
                    "CONSTRAINT fk_deviceSystem_key FOREIGN KEY (deviceKey) REFERENCES device (firebaseKey)," +
                    "CONSTRAINT fk_deviceSystem_key FOREIGN KEY (systemAdress) REFERENCES userSystem (systemAdress)" +
                    ");", // FIM CREATE TABLE*/

            "CREATE TABLE userSystem (" +
                    "systemAdress TEXT PRIMARY KEY," +
                    "systemName TEXT NOT NULL," +
                    "userLogin TEXT NOT NULL," +
                    "dtCadastro TEXT NOT NULL," +
                    "hasSession TEXT NOT NULL" +
                    ");" // FIM CREATE TABLE

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
