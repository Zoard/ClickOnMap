package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 09/10/17.
 */

public class Collaboration implements Serializable {

    private String title;
    private String description;
    private String category;
    private String subcategory;
    private String photo;
    private String video;
    private String audio;
    private Double latitude;
    private Double longitude;

    public Collaboration(String title, String description, String category,
                         String subcategory, String photo, String video, String audio,
                         Double latitude, Double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.photo = photo;
        this.video = video;
        this.audio = audio;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
