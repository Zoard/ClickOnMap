package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.Helper.CallbackItemTouch;
import com.example.zoardgeocze.clickonmap.Helper.ItemTouchHelperCallback;
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

public class MenuActivity extends AppCompatActivity implements CallbackItemTouch{

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
        Log.d("Teste", "Nasci");

        this.menuRecycler.setAdapter(new MenuAdapter(menuTiles,this));
        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);
        this.menuRecycler.setLayoutManager(layout);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(this.menuRecycler);

    }

    private void getSystemsFromDataBase() {
        this.generalController = SingletonFacadeController.getInstance();
        Log.d("Teste", "GeneralController: " + this.generalController);

        for (SystemTile systemTile:this.generalController.getMenuTiles()) {
            this.menuTiles.add(systemTile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Toast.makeText(this,"Não foi possível realizar seu cadastro no sistema.\n Tente novamente.", Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == 1) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Teste", "Matei");
        this.generalController.closeSingleton();
    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        this.menuTiles.add(newPosition,this.menuTiles.remove(oldPosition));
        this.menuRecycler.getAdapter().notifyItemMoved(oldPosition,newPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //TODO:Implementar talvez um Modal aqui confirmando a exclusão
        this.menuTiles.remove(position);
        this.menuRecycler.getAdapter().notifyDataSetChanged();
    }
}
