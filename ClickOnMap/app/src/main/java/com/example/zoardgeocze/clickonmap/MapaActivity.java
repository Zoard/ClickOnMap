package com.example.zoardgeocze.clickonmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.fragments.MapaFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 10/10/17.
 */

public class MapaActivity extends AppCompatActivity {

    public static final String NAME = "MapaActivity";

    private MapaFragment mapaFragment;
    private Intent intent;
    private Bundle bundle;

    private List<Collaboration> collaborations;
    private VGISystem vgiSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        this.collaborations = new ArrayList<>();

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();

        assert this.bundle != null;
        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");
        if(this.vgiSystem != null) {
            String base_url = this.vgiSystem.getAddress() + "/";
            getCollaborationsFromServer(base_url);
        }


    }

    private void getCollaborationsFromServer(String base_url) {
        final ProgressDialog mProgressDialog = new ProgressDialog(MapaActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Carregando Colaborações...");
        mProgressDialog.show();

        new RetrofitClientInitializer(base_url)
                .getCollaborationService()
                .getCollaborationsFromServer("getCollaborations")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Collaboration>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                        Toast.makeText(getBaseContext(),"Nenhuma conexão encontrada no momento.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onNext(List<Collaboration> collaborations) {
                        createMapFragment(collaborations);
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }

    private void createMapFragment(List<Collaboration> collaborations) {
        this.collaborations = collaborations;

        if(!collaborations.isEmpty()) {

            this.bundle.putParcelableArrayList("collaborations", (ArrayList<? extends Parcelable>) this.collaborations);
            this.mapaFragment = new MapaFragment();
            this.mapaFragment.setArguments(this.bundle);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction tx = manager.beginTransaction();
            tx.replace(R.id.map, this.mapaFragment);
            tx.commit();

        } else {

            Toast.makeText(this,"Sistema não possui nenhuma colaboração no momento.", Toast.LENGTH_SHORT).show();
            finish();

        }


    }
}
