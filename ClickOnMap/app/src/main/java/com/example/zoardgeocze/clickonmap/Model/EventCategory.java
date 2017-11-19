package com.example.zoardgeocze.clickonmap.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZoardGeocze on 12/11/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCategory implements Serializable{

    private int id;
    private String description;
    private List<EventType> eventTypes;

    public EventCategory(int id, String description, List<EventType> eventTypes) {
        this. id = id;
        this.description = description;
        this.eventTypes = eventTypes;
    }

    public EventCategory(Parcel in) {
        this.id = in.readInt();
        this.description = in.readString();
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

    @Override
    public String toString() {
        return this.description;
    }
}
