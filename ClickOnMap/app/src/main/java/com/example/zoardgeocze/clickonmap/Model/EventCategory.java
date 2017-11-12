package com.example.zoardgeocze.clickonmap.Model;

import java.util.List;

/**
 * Created by ZoardGeocze on 12/11/17.
 */

public class EventCategory {

    private int id;
    private String description;
    private List<EventType> eventTypes;

    public EventCategory(int id, String description, List<EventType> eventTypes) {
        this. id = id;
        this.description = description;
        this.eventTypes = eventTypes;
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

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
