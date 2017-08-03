package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.R;

import java.util.List;

import retrofit2.Callback;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class AddSystemAdapter extends RecyclerView.Adapter {

    private List<VGISystem> vgiSystems;
    private Context context;

    public AddSystemAdapter(List<VGISystem> vgiSystems, Context context) {
        this.vgiSystems = vgiSystems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tile_system,parent,false);

        AddSystemViewHolder holder = new AddSystemViewHolder(view,context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AddSystemViewHolder holder = (AddSystemViewHolder) viewHolder;

        VGISystem vgiSystem = vgiSystems.get(position);
        holder.setVgiSystem(vgiSystem);

        holder.tileSystemName.setText(vgiSystem.getName());
        holder.tileSystemDescription.setText(vgiSystem.getDescription());

    }

    @Override
    public int getItemCount() {
        return vgiSystems.size();
    }
}
