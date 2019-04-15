package com.example.zoardgeocze.clickonmap.responses;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by ZoardGeocze on 15/04/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CollaborationDataResponse extends DefaultDataResponse {

    public List<Collaboration> collaborations;

}
