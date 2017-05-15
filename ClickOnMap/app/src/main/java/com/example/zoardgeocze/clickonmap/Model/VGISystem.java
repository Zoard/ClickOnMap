package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZoardGeocze on 29/04/17.
 */

public class VGISystem implements Serializable {

    private String adress;
    private String name;
    private String description;
    private String color;
    private List<String> category;
    private int contributions;
    private double latX;
    private double latY;
    private double lngX;
    private double lngY;

    public VGISystem(String adress, String name, String description, String color, List<String> category, int contributions) {
        this.adress = adress;
        this.name = name;
        this.description = description;
        this.color = color;
        this.category = category;
        this.contributions = contributions;
    }

    public VGISystem(String adress, String name, String description, double latX, double latY, double lngX, double lngY) {
        this.adress = adress;
        this.name = name;
        this.description = description;
        this.latX = latX;
        this.latY = latY;
        this.lngX = lngX;
        this.lngY = lngY;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public int getContributions() {
        return contributions;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
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
}
