package com.example.zoardgeocze.clickonmap.Systems;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 15/05/2017.
 */

public class GetSystemsFromServer extends AsyncTask<String,String,String> {

    private static String KEY_SUCCESS = "success";
    public Handler handler = new Handler();
    public int TIMEOUT_CONNECTION = 20000;
    public int TIMEOUT_SOCKET = 20000;
    private ProgressDialog pDialog;
    private Context context;
    private List<VGISystem> vgiSystems = new ArrayList<>();

    public GetSystemsFromServer(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public List<VGISystem> getVgiSystems() {
        return this.vgiSystems;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        pDialog = new ProgressDialog(this.context);
        pDialog.setMessage("Carregando...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        ServerFunctions serverFunctions = new ServerFunctions();
        JSONObject jsonObject = serverFunctions.Systems(this.TIMEOUT_CONNECTION,this.TIMEOUT_SOCKET);

        try {
            String res = jsonObject.getString(KEY_SUCCESS);
            if (Integer.parseInt(res) == 1) {

                //JSONArray devices = jsonObject.getJSONArray("aparelhos");
                JSONArray systems = jsonObject.getJSONArray("sistemas");
                //JSONArray mobSystems = jsonObject.getJSONArray("sistemasmoveis");

                Log.i("GET_SYSTEM_SERVER", String.valueOf(systems.length()));

                for(int i = 0; i < systems.length(); i++) {

                    JSONObject system = systems.getJSONObject(i);
                    String adress = system.getString("ip");
                    String name = system.getString("nome");
                    String description = system.getString("descricao");
                    double latX = system.getDouble("latX");
                    double latY = system.getDouble("latY");
                    double lngX = system.getDouble("longX");
                    double lngY = system.getDouble("longY");

                    VGISystem vgiSystem = new VGISystem(adress,name,description,latX,latY,lngX,lngY);
                    this.vgiSystems.add(vgiSystem);
                    if(this.vgiSystems.isEmpty()) {
                        Log.i("GET_SYSTEM_SERVER", "ESTA VAZIO");
                    } else {
                        Log.i("GET_SYSTEM_SERVER", "ESTA CHEIO");
                    }
                }
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
    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
    }
}
















