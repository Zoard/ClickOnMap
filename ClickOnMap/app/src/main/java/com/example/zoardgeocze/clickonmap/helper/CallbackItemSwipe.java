package com.example.zoardgeocze.clickonmap.helper;

/**
 * Created by ZoardGeocze on 26/02/2018.
 */

public interface CallbackItemSwipe {

    /**
     * Called when an item has been swiped
     * @param position view position
     */

    void onItemDismiss(int position);

    void onItemSend(int position);

}
