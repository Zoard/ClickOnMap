package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.Helper.CallbackItemTouch;
import com.example.zoardgeocze.clickonmap.Helper.ItemTouchHelperCallback;
import com.example.zoardgeocze.clickonmap.Helper.RecyclerItemClickListener;
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
    private ImageView deleteIcon;

    //TODO: Implementar um Observer para o Tile

    public List<Tile> getMenuTiles() {
        return menuTiles;
    }

    public void setMenuTiles(List<Tile> menuTiles) {
        this.menuTiles = menuTiles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.menuRecycler = (RecyclerView) findViewById(R.id.menu_recycler);
        this.deleteIcon = (ImageView) findViewById(R.id.delete_item);

        getSystemsFromDataBase();//Verifica existência de sistemas VGI no banco local

        AddTile addSystems = new AddTile("+");
        this.menuTiles.add(addSystems);

        Log.i("menuTileSize",String.valueOf(menuTiles.size()));
        Log.d("Teste", "Nasci");

        this.menuRecycler.setAdapter(new MenuAdapter(menuTiles,this));
        RecyclerView.LayoutManager layout = new GridLayoutManager(this,2);
        this.menuRecycler.setLayoutManager(layout);

        //TODO: Implementar a deleção do Tile aqui
        /*this.menuRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, this.menuRecycler, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, final int position) {

                deleteIcon.setVisibility(view.VISIBLE);

                menuRecycler.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int y = ItemTouchHelperCallback.y;
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                if (y < -50) {
                                    //deleteIcon.setImageDrawable(getDrawable(R.drawable.delete_icon));
                                } else {
                                    deleteIcon.setImageDrawable(getDrawable(R.drawable.delete_icon));
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if (y < -50) {
                                    menuTiles.remove(position);
                                    menuRecycler.getAdapter().notifyDataSetChanged();
                                    deleteIcon.setImageDrawable(getDrawable(R.drawable.delete_icon));
                                    deleteIcon.setVisibility(View.GONE);
                                    return true;
                                }
                                deleteIcon.setVisibility(View.GONE);
                            case MotionEvent.ACTION_CANCEL:
                                break;
                        }
                        return false;
                    }
                });
            }
        }));*/

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
            Toast.makeText(this,"Não foi possível realizar seu cadastro no sistema. Tente novamente.", Toast.LENGTH_SHORT).show();
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
