package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Services.Locationer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.example.zoardgeocze.clickonmap.observer.VGISystemNotifier;


import java.util.Observable;
import java.util.Observer;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class SystemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                                 Observer{

    private static final String ORIGIN = "origin";
    public static final String NAME = "SystemActivity";

    private SingletonFacadeController generalController;

    private Observable vgiSystemObservable;

    private Locationer location;

    private VGISystem vgiSystem;
    private User user;
    private Intent intent;
    private Bundle bundle;

    private TextView systemName;
    private TextView systemDescription;
    private TextView latitudeValue;
    private TextView longitudeValue;
    private TextView lastCollaboration;
    private TextView mostCollaborations;

    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        this.generalController = SingletonFacadeController.getInstance();

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();
        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        this.user = this.generalController.getUser(this.vgiSystem);

        this.systemName = (TextView) findViewById(R.id.system_name);
        this.systemName.setText(this.vgiSystem.getName());

        this.systemDescription = (TextView) findViewById(R.id.system_description);
        this.systemDescription.setText(this.vgiSystem.getDescription());

        this.latitudeValue = (TextView) findViewById(R.id.latitude_value);
        this.longitudeValue = (TextView) findViewById(R.id.longitude_value);

        String lastColab = this.generalController.getLastCollaboration(this.vgiSystem.getAddress());

        this.lastCollaboration = (TextView) findViewById(R.id.system_last_colab);
        this.lastCollaboration.setText(lastColab);

        String mostColab = this.generalController.getMostCollaborator(this.vgiSystem.getAddress());

        this.mostCollaborations = (TextView) findViewById(R.id.system_most_colab);
        this.mostCollaborations.setText(mostColab);

        this.location = new Locationer(this,this.latitudeValue,this.longitudeValue);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageButton drawerButton = (ImageButton) findViewById(R.id.system_menu_btn);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
                TextView navUserName = (TextView) findViewById(R.id.nav_user_name);
                String userName = "Olá, " + user.getName();
                navUserName.setText(userName);

                TextView navUserEmail = (TextView) findViewById(R.id.nav_user_email);
                navUserEmail.setText(user.getEmail());
            }
        });

        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);

        this.vgiSystemObservable = VGISystemNotifier.getInstance();
        this.vgiSystemObservable.addObserver(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String lastColab = this.generalController.getLastCollaboration(this.vgiSystem.getAddress());

        this.lastCollaboration = (TextView) findViewById(R.id.system_last_colab);
        this.lastCollaboration.setText(lastColab);

        String mostColab = this.generalController.getMostCollaborator(this.vgiSystem.getAddress());

        this.mostCollaborations = (TextView) findViewById(R.id.system_most_colab);
        this.mostCollaborations.setText(mostColab);

        int pendingCollabCounter = this.generalController.getPendingCollaborationsCounter(this.vgiSystem.getAddress(),this.user.getId());

        setPendingCollaborationCounter(R.id.nav_pending_collab,pendingCollabCounter);

    }

    private void setPendingCollaborationCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) this.navigationView.getMenu().findItem(itemId).getActionView();
        //ShapeDrawable shape = (ShapeDrawable) view.getBackground();
        view.setBackgroundColor(count > 0 ? getResources().getColor(R.color.comapRed) :
                                            getResources().getColor(R.color.comapGrey));
        view.setText(count > 0 ? String.valueOf(count) : "0");
        Log.i("CollaborationCounter: ",String.valueOf(count));
    }


    public void goToMap(View view) {

        Intent mapIntent = new Intent(this, MapaActivity.class);
        mapIntent.putExtras(this.bundle);
        startActivity(mapIntent);

    }

    public void goToOptions(View view) {

    }

    public void collaborate(View view) {

        String latText = this.latitudeValue.getText().toString();
        String lngText = this.longitudeValue.getText().toString();

        if(!latText.equals("") && !lngText.equals("")) {

            Intent collabIntent = new Intent(this, CollabActivity.class);

            Double lat = Double.valueOf(latText);
            Double lng = Double.valueOf(lngText);

            collabIntent.putExtra("latitude",lat);
            collabIntent.putExtra("longitude",lng);
            collabIntent.putExtras(this.bundle);

            collabIntent.putExtra(ORIGIN,NAME);

            startActivity(collabIntent);
        } else {
            Toast.makeText(this,"Coordenadas não encontradas.\nAguardando localização do aparelho.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        this.vgiSystemObservable.deleteObserver(this);
        this.location.getClient().disconnect();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_pending_collab) {

            Intent pendingCollabIntent = new Intent(this,PendingCollabActivity.class);
            pendingCollabIntent.putExtras(this.bundle);
            startActivity(pendingCollabIntent);

        } else if (id == R.id.nav_configurations) {

        } else if (id == R.id.nav_logout) {

            this.generalController.vgiSystemLogout(this.vgiSystem.getAddress());
            finish();

        } else if (id == R.id.nav_developers) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Realiza a atualização do objeto VGISystem local, caso alguma alteração seja feita na parte Web
    //Implementação do Padrão de Projeto Observer
    @Override
    public void update(Observable observable, Object arg) {
        if(observable instanceof VGISystemNotifier) {
            VGISystemNotifier vgiSystemNotifier = (VGISystemNotifier) observable;

            if(vgiSystemNotifier.getMessage().equals("change_adress")) {
                if(this.vgiSystem.getAddress().equals(vgiSystemNotifier.getOldAddress())) {
                    this.vgiSystem.setAddress(vgiSystemNotifier.getNewAddress());
                    this.bundle.putSerializable("vgiSystem",this.vgiSystem);
                }
            }

            else if(vgiSystemNotifier.getMessage().equals("delete_system")) {
                //TODO: Implementar um modal avisando que o sistema não está mais disponível para colaboração
                finish();
            }

            else if(vgiSystemNotifier.getMessage().equals("category_change") ||
                    vgiSystemNotifier.getMessage().equals("type_change") ) {
                this.vgiSystem.setCategory(this.generalController.getCategories(this.vgiSystem.getAddress()));
                this.bundle.putSerializable("vgiSystem",this.vgiSystem);
            }
        }
    }
}














