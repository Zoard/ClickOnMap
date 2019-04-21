package com.example.zoardgeocze.clickonmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.R;

import java.util.List;

/**
 * Created by ZoardGeocze on 05/02/2018.
 */

public class PendingCollabAdapter extends RecyclerView.Adapter  {

    private List<Collaboration> pendingCollabs;
    private Context context;
    private VGISystem vgiSystem;

    public PendingCollabAdapter(List<Collaboration> pendingCollabs, Context context, VGISystem vgiSystem) {

        this.pendingCollabs = pendingCollabs;
        this.context = context;
        this.vgiSystem = vgiSystem;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_collab,parent,false);

        PendingCollabViewHolder holder = new PendingCollabViewHolder(view,this.context,this.vgiSystem);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PendingCollabViewHolder viewHolder = (PendingCollabViewHolder) holder;

        viewHolder.setItemPosition(position);

        Collaboration pendingCollab = this.pendingCollabs.get(position);

        viewHolder.setPendingCollaboration(pendingCollab);

        viewHolder.pendingCollabTitle.setText(pendingCollab.getTitle());
        viewHolder.pendingCollabDate.setText(pendingCollab.getCollaborationDate());

        if(!pendingCollab.getPhoto().equals("")) {
            viewHolder.pendingCollabPhoto.setBackground(this.context.getDrawable(R.drawable.photo_on));
        }

        if(!pendingCollab.getVideo().equals("")) {
            viewHolder.pendingCollabVideo.setBackground(this.context.getDrawable(R.drawable.video_on));
        }

    }


    @Override
    public int getItemCount() {
        return this.pendingCollabs.size();
    }
}
