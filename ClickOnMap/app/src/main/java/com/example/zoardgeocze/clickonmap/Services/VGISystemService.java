package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.DTO.VGISystemSync;
import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.responses.DefaultDataResponse;
import com.example.zoardgeocze.clickonmap.responses.EventCategoryDataResponse;
import com.example.zoardgeocze.clickonmap.responses.VGISystemDataResponse;

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
    Observable<DefaultDataResponse> sendMobileSystemToServer(@Field("tag") String tag, @Field("systemAddress") String systemAdress, @Field("firebaseKey") String firebaseKey);

    @GET("VGISystem")
    Observable<VGISystemDataResponse> getVGISystemList(@Query("tag") String tag);

    @GET("mobile")
    Observable<EventCategoryDataResponse> getSystemCategories(@Query("tag") String tag);

}
