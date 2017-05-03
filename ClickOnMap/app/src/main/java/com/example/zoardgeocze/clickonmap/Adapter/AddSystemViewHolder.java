package com.example.zoardgeocze.clickonmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.R;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

class AddSystemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private VGISystem vgiSystem;

    final RelativeLayout tileSystemLayout;
    final TextView tileSystemName;
    final TextView tileSystemDescription;

    public void setVgiSystem(VGISystem vgiSystem) {
        this.vgiSystem = vgiSystem;
    }

    public AddSystemViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;

        this.tileSystemLayout = (RelativeLayout) itemView.findViewById(R.id.tile_system_main);
        this.tileSystemName = (TextView) itemView.findViewById(R.id.tile_system_name);
        this.tileSystemDescription = (TextView) itemView.findViewById(R.id.tile_system_description);

        this.tileSystemLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Log.i("ON_CLICK_addSystemVH",String.valueOf(this.vgiSystem.getContributions()));

        Intent data = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("vgiSystem",vgiSystem);
        data.putExtras(bundle);

        ((Activity)this.context).setResult(1,data);
        ((Activity)this.context).finish();
    }
}
