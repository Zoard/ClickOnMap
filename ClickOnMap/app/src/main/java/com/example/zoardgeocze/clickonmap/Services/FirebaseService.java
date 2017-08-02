package com.example.zoardgeocze.clickonmap.Services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public interface FirebaseService {

    @FormUrlEncoded
    @POST("FCM/")
    Call<String> sendFirebaseKeyToServer(@Field("tag") String tag, @Field("firebaseKey") String firebaseKey, @Field("creationDate") String creationDate);


}
