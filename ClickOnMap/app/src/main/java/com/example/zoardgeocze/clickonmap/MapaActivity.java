package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zoardgeocze.clickonmap.fragments.MapaFragment;

/**
 * Created by ZoardGeocze on 10/10/17.
 */

public class MapaActivity extends AppCompatActivity {

    private MapaFragment mapaFragment;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();

        this.mapaFragment = new MapaFragment();
        this.mapaFragment.setArguments(this.bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        tx.replace(R.id.map, this.mapaFragment);
        tx.commit();
    }
}
