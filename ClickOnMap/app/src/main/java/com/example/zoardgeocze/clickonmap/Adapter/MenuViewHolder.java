package com.example.zoardgeocze.clickonmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.AddSystemActivity;
import com.example.zoardgeocze.clickonmap.LoginActivity;
import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.Tile;
import com.example.zoardgeocze.clickonmap.R;

import org.w3c.dom.Text;


/**
 * Created by ZoardGeocze on 27/04/17.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final TextView tileName;
    final RelativeLayout tileLayout;
    final ImageView tileImg;
    final TextView tileContributions;

    private Context context;
    private Tile tile;
    private boolean addTile;

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setAddTile(boolean addTile) {
        this.addTile = addTile;
    }

    public MenuViewHolder(View itemView, Context context) {

        super(itemView);

        tileName = (TextView) itemView.findViewById(R.id.tile_name);
        tileLayout = (RelativeLayout) itemView.findViewById(R.id.tile_main);
        tileImg = (ImageView) itemView.findViewById(R.id.tile_img);
        tileContributions = (TextView) itemView.findViewById(R.id.tile_contribution);

        this.context = context;
        this.addTile = false;

        tileLayout.setOnClickListener(this);
    }

    //Trata o evento de Clique dos Tiles, passando informações dos Sistemas VGI para a Tela de Login ou Tela do Sistema
    //Treats the Tile's Click event, passing information of the VGI Systems to the Login Screen or System Screen
    @Override
    public void onClick(View view) {

        //Toast.makeText(this.context,"The Item Clicked is: " + this.name.getText(),Toast.LENGTH_SHORT).show();

        if(this.addTile) {
            Log.i("ADD_TILE_CLICK","ADD_TILE_CLICK");

            Intent intent = new Intent(this.context, AddSystemActivity.class);

            ((Activity)this.context).startActivityForResult(intent,1);

        } else {
            Log.i("SYSTEM_TILE_CLICK","SYSTEM_TILE_CLICK");


            //Testando se este Tile aqui está sendo modificado
            if(this.tile instanceof SystemTile) {
                SystemTile systemTile = (SystemTile) tile;
                Toast.makeText(this.context,"Endereço do Tile é: " + systemTile.getSystem().getAdress(),Toast.LENGTH_SHORT).show();
            }


            //TODO: Implementar login do usuário no sistema, verificando a sessão do sistema

        }
    }
}











