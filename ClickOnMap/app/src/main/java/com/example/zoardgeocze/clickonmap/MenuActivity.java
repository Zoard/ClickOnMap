package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;
import com.example.zoardgeocze.clickonmap.FCM.FirebaseIDService;
import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitInitializer;
import com.example.zoardgeocze.clickonmap.Server.GetSystemsFromServer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonDataBase;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class MenuActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private List<Tile> menuTiles = new ArrayList<>();
    //private List<VGISystem> vgiSystems = new ArrayList<>();

    private RecyclerView menuRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.menuRecycler = (RecyclerView) findViewById(R.id.menu_recycler);

        this.generalController = SingletonFacadeController.getInstance();

        getSystemsFromServer(); //Pega os sistemas no Server Utilizando Retrofit

        final GetSystemsFromServer serverSystems = new GetSystemsFromServer(this); // Pega os sistemas no server Utilizando método antigo
        serverSystems.execute();

        List<VGISystem> vgiSystems = serverSystems.getVgiSystems();

        if(vgiSystems.isEmpty()) {
            Log.i("verifica_lista","Esta Vazio");
        }

        for (SystemTile systemTile:this.generalController.getMenuTiles()) {
            this.menuTiles.add(systemTile);
        }

        AddTile addSystems = new AddTile("+",vgiSystems);
        this.menuTiles.add(addSystems);


        Log.i("menuTileSize",String.valueOf(menuTiles.size()));
        this.menuRecycler.setAdapter(new MenuAdapter(menuTiles,this));

        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);
        this.menuRecycler.setLayoutManager(layout);

    }

    private void getSystemsFromServer() {

        Call<VGISystemSync> call = new RetrofitInitializer().getSystemService().getVGISystemList("getVGISystem");
        call.enqueue(new Callback<VGISystemSync>() {
            @Override
            public void onResponse(Call<VGISystemSync> call, Response<VGISystemSync> response) {
                VGISystemSync vgiSystemSync = response.body();
                List<VGISystem> vgiSystems = vgiSystemSync.getVgiSystems();
                Log.i("onResponse_VGISYSTEM: ", String.valueOf(vgiSystems.size()));
            }

            @Override
            public void onFailure(Call<VGISystemSync> call, Throwable t) {
                Log.i("onFailure_VGISYSTEM: ", t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            VGISystem vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");
            User user = (User) bundle.getSerializable("user");

            this.generalController.registerUser(this,vgiSystem,user);

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
