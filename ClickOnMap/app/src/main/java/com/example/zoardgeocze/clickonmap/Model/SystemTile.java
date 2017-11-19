package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class SystemTile extends Tile implements Serializable {

    private VGISystem system;

    private boolean available;

    public SystemTile(VGISystem system) {
        this.system = system;
        this.available = true;
    }

    public VGISystem getSystem() {
        return this.system;
    }

    public void setSystem(VGISystem system) {
        this.system = system;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
