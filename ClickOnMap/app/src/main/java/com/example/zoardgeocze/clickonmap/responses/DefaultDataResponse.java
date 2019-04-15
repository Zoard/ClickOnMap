package com.example.zoardgeocze.clickonmap.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by ZoardGeocze on 15/04/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultDataResponse implements Serializable {

    public String tag;
    public int success;
    public int error;
    public String error_msg;

}
