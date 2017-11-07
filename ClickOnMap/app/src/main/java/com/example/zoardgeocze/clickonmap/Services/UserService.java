package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ZoardGeocze on 09/08/2017.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("clickonmap_mobile/")
    Call<String> sendUserToServer(@Field("tag") String tag,
                                  @Field("email") String email,
                                  @Field("name") String name,
                                  @Field("password") String password,
                                  @Field("type") char type,
                                  @Field("registerDate") String registerDate);

    @FormUrlEncoded
    @POST("clickonmap_mobile/")
    Call<String> verifyUser(@Field("tag") String tag, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @GET("clickonmap_mobile")
    Call<User> getUserFromServer(@Field("tag") String tag, @Field("email") String email);

}
