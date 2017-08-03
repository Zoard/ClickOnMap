package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class MenuActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private List<Tile> menuTiles = new ArrayList<>();

    private RecyclerView menuRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.menuRecycler = (RecyclerView) findViewById(R.id.menu_recycler);

        getSystemsFromDataBase();//Verifica existência de sistemas VGI no banco local

        AddTile addSystems = new AddTile("+");
        this.menuTiles.add(addSystems);

        Log.i("menuTileSize",String.valueOf(menuTiles.size()));

        this.menuRecycler.setAdapter(new MenuAdapter(menuTiles,this));

        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);

        this.menuRecycler.setLayoutManager(layout);

    }

    private void getSystemsFromDataBase() {
        this.generalController = SingletonFacadeController.getInstance();

        for (SystemTile systemTile:this.generalController.getMenuTiles()) {
            this.menuTiles.add(systemTile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            VGISystem vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");
            User user = (User) bundle.getSerializable("user");

            this.generalController.registerUser(this,vgiSystem,user);

            SystemTile systemTile = new SystemTile(vgiSystem);

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
