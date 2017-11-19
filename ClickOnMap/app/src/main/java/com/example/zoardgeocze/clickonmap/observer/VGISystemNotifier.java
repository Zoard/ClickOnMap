package com.example.zoardgeocze.clickonmap.observer;

import java.util.Observable;

/**
 * Created by ZoardGeocze on 19/11/17.
 */

public class VGISystemNotifier extends Observable {

    private String message;
    private String oldAddress;
    private String newAddress;
    private static VGISystemNotifier INSTANCE = null;

    public static VGISystemNotifier getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new VGISystemNotifier();
        }
        return INSTANCE;
    }

    public void setVGIsystemNotifier(String message, String oldAddress, String newAddress) {
        this.message = message;
        this.oldAddress = oldAddress;
        this.newAddress = newAddress;
        setChanged();
        notifyObservers();
    }

    public String getMessage() {
        return message;
    }

    public String getOldAddress() {
        return oldAddress;
    }

    public String getNewAddress() {
        return newAddress;
    }
}
