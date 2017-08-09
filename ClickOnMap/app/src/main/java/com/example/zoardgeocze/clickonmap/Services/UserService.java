package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by ZoardGeocze on 09/08/2017.
 */

public interface UserService {

    @POST("/User/")
    Call<String> sendUserToServer(@Field("tag") String tag, @Body User user);

}
