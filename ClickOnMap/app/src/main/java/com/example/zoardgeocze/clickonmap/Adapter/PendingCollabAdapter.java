package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.R;

import java.util.List;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabAdapter extends RecyclerView.Adapter {

    private List<Collaboration> pendingCollabs;
    private Context context;

    public PendingCollabAdapter(List<Collaboration> pendingCollabs, Context context) {

        this.pendingCollabs = pendingCollabs;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_collab,parent,false);

        PendingCollabViewHolder holder = new PendingCollabViewHolder(view,context);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PendingCollabViewHolder viewHolder = (PendingCollabViewHolder) holder;

        viewHolder.pendingCollabTitle.setText(this.pendingCollabs.get(position).getTitle());
        viewHolder.pendingCollabDate.setText(this.pendingCollabs.get(position).getCollaborationDate());

    }


    @Override
    public int getItemCount() {
        return this.pendingCollabs.size();
    }
}
