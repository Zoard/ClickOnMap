package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class AddTile extends Tile implements Serializable {

    private List<VGISystem> vgiSystems;

    public AddTile(String name, List<VGISystem> vgiSystems) {
        super(name);
        this.vgiSystems = vgiSystems;
    }

    public List<VGISystem> getVgiSystems() {
        return vgiSystems;
    }

    public void setVgiSystems(List<VGISystem> vgiSystems) {
        this.vgiSystems = vgiSystems;
    }
}
