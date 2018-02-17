package com.example.zoardgeocze.clickonmap.helper;

/**
 * Created by ZoardGeocze on 10/08/2017.
 */

public interface CallbackItemTouch {

    /**
     * Called when an item has been dragged
     * @param oldPosition start position
     * @param newPosition end position
     */
    void itemTouchOnMove(int oldPosition,int newPosition);

    void onItemDismiss(int position);

}
