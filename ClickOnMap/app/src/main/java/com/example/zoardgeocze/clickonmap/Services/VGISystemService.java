package com.example.zoardgeocze.clickonmap.Services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by zoard_000 on 01/08/2017.
 */

public interface VGISystemService {

    @POST("systems")
    Call<VGISystemService> lista();

}
