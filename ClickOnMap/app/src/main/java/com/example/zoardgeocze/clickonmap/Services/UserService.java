package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.DTO.UserSync;
import com.example.zoardgeocze.clickonmap.Model.User;
import com.example.zoardgeocze.clickonmap.responses.DefaultDataResponse;
import com.example.zoardgeocze.clickonmap.responses.UserDataResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ZoardGeocze on 09/08/2017.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("mobile/")
    Call<DefaultDataResponse> sendUserToServer(@Field("tag") String tag,
                                               @Field("email") String email,
                                               @Field("name") String name,
                                               @Field("password") String password,
                                               @Field("type") char type,
                                               @Field("registerDate") String registerDate,
                                               @Field("firebaseKey") String firebaseKey);

    @FormUrlEncoded
    @POST("mobile/")
    Call<UserDataResponse> verifyUser(@Field("tag") String tag, @Field("email") String email, @Field("password") String password,
                            @Field("firebaseKey") String firebaseKey);

    @FormUrlEncoded
    @POST("mobile/")
    Observable<UserDataResponse> getUserFromServer(@Field("tag") String tag, @Field("email") String email);

}
