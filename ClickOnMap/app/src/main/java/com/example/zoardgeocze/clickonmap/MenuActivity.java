package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.FCM.FirebaseIDService;
import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Server.GetSystemsFromServer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonDataBase;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private List<Tile> menuTiles = new ArrayList<>();
    private RecyclerView menuRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.menuRecycler = (RecyclerView) findViewById(R.id.menu_recycler);

        SingletonDataBase db = SingletonDataBase.getInstance();

        //Demonstração do Aplicativo para possíveis sistemas colaborativos
        //App Demo for possible colaborative Systems
        /*
        List<VGISystem> vgiSystems = new ArrayList<>();

        List<String> category = new ArrayList<>();
        category.add("Segurança");

        VGISystem vgiSystem = new VGISystem("192.168.1.1","Cidadão Viçosa", "Sistema Colaborativo para melhorar condição da cidade de Viçosa","#FFFFFF",category,20);
        VGISystem vgiSystem_2 = new VGISystem("192.168.0.1","Gota D'Água", "Sistema Colaborativo para diminuir o desperdício de Água","#FFFFFF",category,35);
        VGISystem vgiSystem_3 = new VGISystem("192.168.0.0","Cidade Linda", "Sistema Colaborativo para melhorias da cidade de São Paulo","#FFFFFF",category,101);

        vgiSystems.add(vgiSystem);
        vgiSystems.add(vgiSystem_2);
        vgiSystems.add(vgiSystem_3);
        */
        final GetSystemsFromServer serverSystems = new GetSystemsFromServer(this);
        serverSystems.execute();

        List<VGISystem> vgiSystems = serverSystems.getVgiSystems();

        if(vgiSystems.isEmpty()) {
            Log.i("verifica_lista","Esta Vazio");
        }

        AddTile addSystems = new AddTile("+",vgiSystems);

        this.menuTiles.add(0,addSystems);

        Log.i("menuTileSize",String.valueOf(menuTiles.size()));
        this.menuRecycler.setAdapter(new MenuAdapter(menuTiles,this));

        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);
        this.menuRecycler.setLayoutManager(layout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            VGISystem vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

            SystemTile systemTile = new SystemTile(vgiSystem.getName(),vgiSystem);

            this.menuTiles.add(0,systemTile);

            //Toast.makeText(this,String.valueOf(vgiSystem.getContributions()),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.menuRecycler.getAdapter().notifyDataSetChanged();
    }
}
