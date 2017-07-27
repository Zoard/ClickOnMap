package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Adapter.AddSystemAdapter;
import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class AddSystemActivity extends AppCompatActivity {

    //TODO: Verificar se sistema já existe no menu
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_system);

        RecyclerView addSystemRecycler = (RecyclerView) findViewById(R.id.add_system_recycler);

        List<VGISystem> vgiSystems = new ArrayList<>();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        AddTile addTile = (AddTile) bundle.getSerializable("tile");

        vgiSystems = addTile.getVgiSystems();

        addSystemRecycler.setAdapter(new AddSystemAdapter(vgiSystems,this));

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        addSystemRecycler.setLayoutManager(layout);
    }

    public void backToMenu(View view) {
        finish();
    }
}
