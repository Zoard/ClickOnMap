package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.R;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener, View.OnCreateContextMenuListener {

    private static final int SEND_ID = 1;
    private static final int EDIT_ID = 2;
    private static final int DELETE_ID = 3;

    private int itemPosition;

    final TextView pendingCollabTitle;
    final TextView pendingCollabDate;

    final RelativeLayout pendingCollabLayout;

    public PendingCollabViewHolder(View itemView) {

        super(itemView);

        this.pendingCollabTitle = (TextView) itemView.findViewById(R.id.pending_collab_title);
        this.pendingCollabDate = (TextView) itemView.findViewById(R.id.pending_collab_date);

        this.pendingCollabLayout = (RelativeLayout) itemView.findViewById(R.id.pending_collab_main);
        this.pendingCollabLayout.setOnClickListener(this);
        this.pendingCollabLayout.setOnLongClickListener(this);
        this.pendingCollabLayout.setOnCreateContextMenuListener(this);

    }


    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    @Override
    public void onClick(View view) {
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
