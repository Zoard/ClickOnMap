package com.example.zoardgeocze.clickonmap.responses;

import com.example.zoardgeocze.clickonmap.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ZoardGeocze on 15/04/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataResponse extends DefaultDataResponse {

    public User user;

}
