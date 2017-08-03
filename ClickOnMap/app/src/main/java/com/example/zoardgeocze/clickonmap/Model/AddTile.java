package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class AddTile extends Tile implements Serializable {
    private String name;

    public AddTile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
