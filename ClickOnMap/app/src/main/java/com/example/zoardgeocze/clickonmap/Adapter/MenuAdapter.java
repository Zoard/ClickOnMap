package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.AddTile;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.R;
import com.example.zoardgeocze.clickonmap.Model.Tile;

import java.util.List;

/**
 * Created by ZoardGeocze on 27/04/17.
 */

public class MenuAdapter extends RecyclerView.Adapter {

    private List<Tile> tiles;
    private Context context;

    public MenuAdapter(List<Tile> tiles, Context context) {
        this.tiles = tiles;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tile,parent,false);

        MenuViewHolder holder = new MenuViewHolder(view,context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        MenuViewHolder holder = (MenuViewHolder) viewHolder;

        if(tiles.get(position) instanceof AddTile) {

            AddTile tile = (AddTile) tiles.get(position);

            holder.setTile(tile);
            holder.setAddTile(true);
            holder.tileImg.setBackgroundResource(R.drawable.add_tile_img);

        } else {

            SystemTile tile = (SystemTile) tiles.get(position);

            holder.setTile(tile);
            holder.setAddTile(false);
            holder.tileName.setText(tile.getSystem().getName());
            holder.tileContributions.setText(String.valueOf(tile.getSystem().getCollaborations()) + " Colaborações");

            //Adicionar Algum Ícone Específico do Sistema VGI
            //Add Some Specified Icon from the VGI System
            holder.tileImg.setBackgroundResource(0);

        }


    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }
}
