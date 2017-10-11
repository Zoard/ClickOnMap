package com.example.zoardgeocze.clickonmap.Model;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 09/10/17.
 */

public class Collaboration implements Serializable {

    private String userId;
    private String title;
    private String description;
    private String collaborationDate;
    private int categoryId;
    private String categoryName;
    private int subcategoryId;
    private String subcategoryName;
    private String photo;
    private String video;
    private String audio;
    private Double latitude;
    private Double longitude;

    public Collaboration() {
        this.userId = "";
        this.title = "";
        this.description = "";
        this.collaborationDate = "";
        this.categoryId = 0;
        this.categoryName = "";
        this.subcategoryId = 0;
        this.subcategoryName = "";
        this.photo = "";
        this.video = "";
        this.audio = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Collaboration(String userId, String title, String description, String collaborationDate, int categoryId, String categoryName,
                         int subcategoryId, String subcategoryName, String photo, String video, String audio,
                         Double latitude, Double longitude) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.collaborationDate = collaborationDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.photo = photo;
        this.video = video;
        this.audio = audio;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCollaborationDate() {
        return collaborationDate;
    }

    public void setCollaborationDate(String collaborationDate) {
        this.collaborationDate = collaborationDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
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
