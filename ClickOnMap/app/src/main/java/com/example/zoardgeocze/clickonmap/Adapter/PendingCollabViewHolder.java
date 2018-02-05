package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.R;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    private Context context;

    final TextView pendingCollabTitle;

    public PendingCollabViewHolder(View itemView, Context context) {

        super(itemView);

        this.context = context;

        this.pendingCollabTitle = (TextView) itemView.findViewById(R.id.pending_collab_title);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
