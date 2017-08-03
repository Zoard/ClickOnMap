package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class SystemTile extends Tile implements Serializable {

    private VGISystem system;

    public SystemTile(VGISystem system) {
        this.system = system;
    }

    public VGISystem getSystem() {
        return this.system;
    }

    public void setSystem(VGISystem system) {
        this.system = system;
    }

}
