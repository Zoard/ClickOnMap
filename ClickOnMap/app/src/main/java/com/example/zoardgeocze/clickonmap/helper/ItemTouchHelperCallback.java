package com.example.zoardgeocze.clickonmap.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zoardgeocze.clickonmap.R;
import com.example.zoardgeocze.clickonmap.Util.MyApp;

/**
 * Created by ZoardGeocze on 10/08/2017.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    CallbackItemTouch callbackItemTouch;

    CallbackItemSwipe callbackItemSwipe;

    public static final float ALPHA_FULL = 1.0f;

    public static int y;

    public ItemTouchHelperCallback(CallbackItemTouch callbackItemTouch){
        this.callbackItemTouch = callbackItemTouch;
    }

    public ItemTouchHelperCallback(CallbackItemSwipe callbackItemSwipe) {
        this.callbackItemSwipe = callbackItemSwipe;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = 0;
            final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        callbackItemTouch.itemTouchOnMove(viewHolder.getAdapterPosition(),target.getAdapterPosition()); // information to the interface
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        switch (direction) {

            case ItemTouchHelper.LEFT:
                callbackItemSwipe.onItemDismiss(viewHolder.getAdapterPosition());
                break;

            case ItemTouchHelper.RIGHT:
                callbackItemSwipe.onItemSend(viewHolder.getAdapterPosition());
                break;

        }

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = viewHolder.itemView.findViewById(R.id.pending_collab_main);
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d("ON_CHILD_DRAW", "SWIPE");
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final View foregroundView = viewHolder.itemView.findViewById(R.id.pending_collab_main);
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
            View backgroundViewDelete = viewHolder.itemView.findViewById(R.id.pending_collab_swipe_dismiss);
            View backgroundViewSend = viewHolder.itemView.findViewById(R.id.pending_collab_swipe_send);
            //viewHolder.itemView.addTouchables
            //final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            //viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
            y = (int) viewHolder.itemView.getY();

            if (dX < 0) {
                Log.d("ON_CHILD_DRAW_OVER", "SWIPE_LEFT");
                backgroundViewDelete.setVisibility(View.VISIBLE);
                backgroundViewSend.setVisibility(View.INVISIBLE);
            }

            if (dX > 0) {
                Log.d("ON_CHILD_DRAW_OVER", "SWIPE_RIGHT");
                backgroundViewDelete.setVisibility(View.INVISIBLE);
                backgroundViewSend.setVisibility(View.VISIBLE);
            }


        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d("ON_CHILD_DRAW_OVER", "SWIPE");
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            // Fade out the view as it is swiped out of the parent's bounds
            final View foregroundView = viewHolder.itemView.findViewById(R.id.pending_collab_main);
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
            View backgroundViewDelete = viewHolder.itemView.findViewById(R.id.pending_collab_swipe_dismiss);
            View backgroundViewSend = viewHolder.itemView.findViewById(R.id.pending_collab_swipe_send);
            //viewHolder.itemView.addTouchables
            //final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            //viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
            y = (int) viewHolder.itemView.getY();

            if (dX < 0) {
                Log.d("ON_CHILD_DRAW_OVER", "SWIPE_LEFT");
                backgroundViewDelete.setVisibility(View.VISIBLE);
                backgroundViewSend.setVisibility(View.INVISIBLE);
            }

            if (dX > 0) {
                Log.d("ON_CHILD_DRAW_OVER", "SWIPE_RIGHT");
                backgroundViewDelete.setVisibility(View.INVISIBLE);
                backgroundViewSend.setVisibility(View.VISIBLE);
            }


        } else {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
