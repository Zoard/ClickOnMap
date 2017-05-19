package com.example.zoardgeocze.clickonmap.Helper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 10/05/2017.
 */

public class ServerFunctions {

    private JSONParser jsonParser;
    private static String SYSTEMS = "systems";
    private static String INSERT = "insere";
    private static String URL = "https://ufvmaps.fornut.com.br/clickonmap/android_index.php";

    public ServerFunctions() {
        this.jsonParser = new JSONParser();
    }

    public JSONObject Systems(int timeConnection, int timeSocket) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("tag", SYSTEMS));
        JSONObject json = this.jsonParser.getJSONFromUrl(URL, params, timeConnection, timeSocket);
        return json;
    }

    public JSONObject Insert(String table, String column, String value, int timeConnection, int timeSocket){

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("tag", INSERT));
        params.add(new BasicNameValuePair("tabela", table));
        params.add(new BasicNameValuePair("campos", column));
        params.add(new BasicNameValuePair("valores", value));

        JSONObject json = this.jsonParser.getJSONFromUrl(URL, params, timeConnection, timeSocket);
        return json;
    }
}
