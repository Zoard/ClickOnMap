package com.example.zoardgeocze.clickonmap.Model;

/**
 * Created by ZoardGeocze on 12/11/17.
 */

public class EventType {

    private int id;
    private String description;

    public EventType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
