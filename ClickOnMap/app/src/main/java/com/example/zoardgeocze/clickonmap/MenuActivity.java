package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.ImageView;

import com.example.zoardgeocze.clickonmap.Adapter.MenuAdapter;
import com.example.zoardgeocze.clickonmap.helper.CallbackItemTouch;
import com.example.zoardgeocze.clickonmap.helper.ItemTouchHelperCallback;
import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.fcm.ClickOnMapFirebaseMessagingService;
import com.example.zoardgeocze.clickonmap.observer.VGISystemNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by ZoardGeocze on 03/05/2017.
 */

public class MenuActivity extends AppCompatActivity implements CallbackItemTouch,Observer{

    private SingletonFacadeController generalController;

    private Observable vgiSystemObservable;

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

        this.vgiSystemObservable = VGISystemNotifier.getInstance();
        this.vgiSystemObservable.addObserver(this);

    }

    private void handleData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String message = bundle.getString("message");
            String oldAddress = bundle.getString("oldAdress");
            String newAddress = bundle.getString("newAdress");
            Log.i("HANDLE_DATA: ",message);
            Log.i("HANDLE_DATA: ",oldAddress);
            Log.i("HANDLE_DATA: ",newAddress);
            ClickOnMapFirebaseMessagingService comapMsgService = new ClickOnMapFirebaseMessagingService();
            comapMsgService.onMessageReceivedSystemTray(message,oldAddress,newAddress);
        }
    }

    private void getSystemsFromDataBase() {
        this.generalController = SingletonFacadeController.getInstance();
        Log.d("Teste", "GeneralController: " + this.generalController);

        for (SystemTile systemTile:this.generalController.getMenuTiles()) {
            this.menuTiles.add(systemTile);
        }
    }

    //TODO: Melhorar esses requests e results codes, estão desorganizados
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            //Toast.makeText(this,"Nenhum Sistema VGI foi cadastrado.", Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            VGISystem vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

            User user = (User) bundle.getSerializable("user");

            //Se der True é porque ainda não existe Tile do Sistema no Hub, nesse caso é necessário atualizar
            //caso contrário, não adiciona mais Tiles
            if(this.generalController.registerUser(vgiSystem,user)) {
                this.generalController.registerCategory(vgiSystem); //Registra Categorias no Sistema
                SystemTile systemTile = new SystemTile(vgiSystem);
                this.menuTiles.add(0,systemTile);
            }
            //Toast.makeText(this,String.valueOf(vgiSystem.getContributions()),Toast.LENGTH_SHORT).show();
        }
        else if(resultCode == 3) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleData();//Handle FCM messages that are in System Tray
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
        this.vgiSystemObservable.deleteObserver(this);
    }

    @Override
    public void itemTouchOnMove(int oldPosition, int newPosition) {
        this.menuTiles.add(newPosition,this.menuTiles.remove(oldPosition));
        this.menuRecycler.getAdapter().notifyItemMoved(oldPosition,newPosition);
    }

    /*@Override
    public void onItemDismiss(int position) {
        //TODO:Implementar talvez um Modal aqui confirmando a exclusão
        this.menuTiles.remove(position);
        this.menuRecycler.getAdapter().notifyDataSetChanged();
    }*/

    //Realiza a atualização dos objetos VGISystem locais, caso alguma alteração seja feita na parte Web
    //Implementação do Padrão de Projeto Observer
    @Override
    public void update(Observable observable, Object arg) {

        if(observable instanceof VGISystemNotifier) {
            VGISystemNotifier vgiSystemNotifier = (VGISystemNotifier) observable;

            if(vgiSystemNotifier.getMessage().equals("change_adress")) {
                for(int i=0; i < this.menuTiles.size(); i++) {
                    if(this.menuTiles.get(i) instanceof SystemTile) {
                        if(((SystemTile) this.menuTiles.get(i))
                                .getSystem().getAddress().equals(vgiSystemNotifier.getOldAddress())) {
                            ((SystemTile) this.menuTiles.get(i)).getSystem().setAddress(vgiSystemNotifier.getNewAddress());
                        }
                    }
                }
            }

            else if(vgiSystemNotifier.getMessage().equals("delete_system")) {
                for(int i=0; i < this.menuTiles.size(); i++) {
                    if(this.menuTiles.get(i) instanceof SystemTile) {
                        if(((SystemTile) this.menuTiles.get(i))
                                .getSystem().getAddress().equals(vgiSystemNotifier.getOldAddress())) {
                            ((SystemTile) this.menuTiles.get(i)).setAvailable(false);
                        }
                    }
                }
            }

            else if(vgiSystemNotifier.getMessage().equals("category_change") ||
                    vgiSystemNotifier.getMessage().equals("type_change") ) {
                for(int i=0; i < this.menuTiles.size(); i++) {
                    if(this.menuTiles.get(i) instanceof SystemTile) {
                        if(((SystemTile) this.menuTiles.get(i))
                                .getSystem().getAddress().equals(vgiSystemNotifier.getOldAddress())) {

                            ((SystemTile) this.menuTiles
                                    .get(i))
                                    .getSystem()
                                    .setCategory(this.generalController.getCategories(vgiSystemNotifier.getOldAddress()));
                        }
                    }
                }
            }
        }

    }

}
