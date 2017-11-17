package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;
import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public interface VGISystemService {

    @FormUrlEncoded
    @POST("VGISystem/")
    Observable<String> sendMobileSystemToServer(@Field("tag") String tag, @Field("systemAdress") String systemAdress, @Field("firebaseKey") String firebaseKey);

    @GET("VGISystem")
    Observable<List<VGISystem>> getVGISystemList(@Query("tag") String tag);

    @GET("mobile")
    Observable<List<EventCategory>> getSystemCategories(@Query("tag") String tag);

}
