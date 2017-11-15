package com.example.zoardgeocze.clickonmap.Model;

import android.util.Log;

import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by ZoardGeocze on 29/04/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VGISystem implements Serializable, Observer {

    private String address;
    private String name;
    private String description;
    private String color;
    private List<EventCategory> category; //TODO: Verificar se uma tabela Hash aqui seria melhor
    private int collaborations;
    private double latX;
    private double latY;
    private double lngX;
    private double lngY;

    public VGISystem() {
        this.address = "";
        this.name = "";
        this.description = "";
        this.color = "";
        this.collaborations = 0;
        this.latX = 0.0;
        this.latY = 0.0;
        this.lngX = 0.0;
        this.lngY = 0.0;
    }

    public VGISystem(String address, String name, String description, String color, List<EventCategory> category, int collaborations) {
        this.address = address;
        this.name = name;
        this.description = description;
        this.color = color;
        this.category = category;
        this.collaborations = collaborations;
    }

    public VGISystem(String address, String name, String description, int collaborations, double latX, double latY, double lngX, double lngY) {
        this.address = address;
        this.name = name;
        this.description = description;
        this.color = "";
        this.collaborations = collaborations;
        this.latX = latX;
        this.latY = latY;
        this.lngX = lngX;
        this.lngY = lngY;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(int collaborations) {
        this.collaborations = collaborations;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<EventCategory> getCategory() {
        return category;
    }

    public void setCategory(List<EventCategory> category) {
        this.category = category;
    }

    public double getLatX() {
        return latX;
    }

    public void setLatX(double latX) {
        this.latX = latX;
    }

    public double getLatY() {
        return latY;
    }

    public void setLatY(double latY) {
        this.latY = latY;
    }

    public double getLngX() {
        return lngX;
    }

    public void setLngX(double lngX) {
        this.lngX = lngX;
    }

    public double getLngY() {
        return lngY;
    }

    public void setLngY(double lngY) {
        this.lngY = lngY;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.address = arg.toString();
        Log.i("update_OBSERVER: ",arg.toString());
    }
}
