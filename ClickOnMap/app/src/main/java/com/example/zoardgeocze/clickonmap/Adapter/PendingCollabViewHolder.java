package com.example.zoardgeocze.clickonmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.CollabActivity;
import com.example.zoardgeocze.clickonmap.ContentActivity;
import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.PendingCollabActivity;
import com.example.zoardgeocze.clickonmap.R;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener, View.OnCreateContextMenuListener {

    private static final int SEND_ID = 1;
    private static final int EDIT_ID = 2;
    private static final int DELETE_ID = 3;

    private static final String ORIGIN = "origin";

    private int itemPosition;
    private Context context;
    private Collaboration pendingCollaboration;
    private VGISystem vgiSystem;

    final TextView pendingCollabTitle;
    final TextView pendingCollabDate;
    final ImageView pendingCollabPhoto;
    final ImageView pendingCollabVideo;

    final RelativeLayout pendingCollabLayout;

    public PendingCollabViewHolder(View itemView, Context context, VGISystem vgiSystem) {

        super(itemView);

        this.context = context;
        this.vgiSystem = vgiSystem;

        this.pendingCollabTitle = (TextView) itemView.findViewById(R.id.pending_collab_title);
        this.pendingCollabDate = (TextView) itemView.findViewById(R.id.pending_collab_date);
        this.pendingCollabPhoto = (ImageView) itemView.findViewById(R.id.pending_collab_photo_icon);
        this.pendingCollabVideo = (ImageView) itemView.findViewById(R.id.pending_collab_video_icon);

        this.pendingCollabLayout = (RelativeLayout) itemView.findViewById(R.id.pending_collab_main);
        this.pendingCollabLayout.setOnClickListener(this);
        this.pendingCollabLayout.setOnLongClickListener(this);
        this.pendingCollabLayout.setOnCreateContextMenuListener(this);

    }

    public void setPendingCollaboration(Collaboration pendingCollaboration) {
        this.pendingCollaboration = pendingCollaboration;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this.context, ContentActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("collab",this.pendingCollaboration);
        bundle.putSerializable("vgiSystem",this.vgiSystem);

        intent.putExtras(bundle);
        intent.putExtra(ORIGIN,PendingCollabActivity.NAME);

        ((Activity)this.context).startActivity(intent);

        Log.i("onClick_Pending: ","Clique Curto");


    }

    @Override
    public boolean onLongClick(View view) {

        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Opções");
        menu.add(this.itemPosition, SEND_ID, Menu.NONE,"Enviar");
        menu.add(this.itemPosition, EDIT_ID, Menu.NONE, "Editar");
        menu.add(this.itemPosition, DELETE_ID, Menu.NONE, "Deletar");

    }


}
