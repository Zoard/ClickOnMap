package com.example.zoardgeocze.clickonmap.Server;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Helper.ServerFunctions;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.security.AccessController.getContext;

/**
 * Created by ZoardGeocze on 17/05/2017.
 */

public class SendFirebaseKeyToServer extends AsyncTask<String, String, String> {

    private static String KEY_SUCCESS = "success";
    private static String TABLE = "aparelhos";
    private static String COLUMN = "chaveFCM";
    public Handler handler = new Handler();
    public int TIMEOUT_CONNECTION = 20000;
    public int TIMEOUT_SOCKET = 20000;
    private ProgressDialog pDialog;
    private Context context;
    private String firebaseKey;

    public SendFirebaseKeyToServer(Context context, String firebaseKey) {
        this.context = context;
        this.firebaseKey = firebaseKey;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onPreExecute() {
        this.pDialog = ProgressDialog.show(this.context, "Aguarde", "Enviando chave para o servidor...", true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        ServerFunctions serverFunctions = new ServerFunctions();
        JSONObject jsonObject = serverFunctions.Insert(this.TABLE, this.COLUMN, this.firebaseKey, this.TIMEOUT_CONNECTION,this.TIMEOUT_SOCKET);

        try {
            String res = jsonObject.getString(KEY_SUCCESS);
            if (Integer.parseInt(res) == 1) {
                Toast.makeText(getContext(), "Chave Firebase inserida com sucesso!", Toast.LENGTH_LONG).show();
            }

        } catch(Exception e) {
            e.printStackTrace();
            handler.post(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(getContext(), "Serviço indisponível no momento...", Toast.LENGTH_LONG).show();
                }
            });
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        this.pDialog.dismiss();
    }
}
