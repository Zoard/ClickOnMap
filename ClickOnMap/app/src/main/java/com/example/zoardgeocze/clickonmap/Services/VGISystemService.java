package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public interface VGISystemService {

    @GET("VGISystem")
    Call<VGISystemSync> getVGISystemList(@Query("tag") String tag);

}
