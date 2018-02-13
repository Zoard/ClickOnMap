package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.R;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    private Context context;

    final TextView pendingCollabTitle;
    final TextView pendingCollabDate;

    final RelativeLayout pendingCollabLayout;

    public PendingCollabViewHolder(View itemView, Context context) {

        super(itemView);

        this.context = context;

        this.pendingCollabTitle = (TextView) itemView.findViewById(R.id.pending_collab_title);
        this.pendingCollabDate = (TextView) itemView.findViewById(R.id.pending_collab_date);

        this.pendingCollabLayout = (RelativeLayout) itemView.findViewById(R.id.pending_collab_main);
        this.pendingCollabLayout.setOnClickListener(this);
        this.pendingCollabLayout.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("onClick_Pending: ","Clique Curto");
    }

    @Override
    public boolean onLongClick(View view) {

        Log.i("onLongClick_Pending: ","Clique Longo");

        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE, 1, Menu.NONE, "Deletar");
                menu.add(Menu.NONE, 2, Menu.NONE,"Enviar");
            }

        });


        return false;
    }




}
