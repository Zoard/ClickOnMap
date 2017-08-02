package com.example.zoardgeocze.clickonmap.Retrofit;

import com.example.zoardgeocze.clickonmap.Services.FirebaseService;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public class RetrofitInitializer {

    private static final String BASE_URL = "http://ufvmaps.fornut.com.br/clickonmap/";
    private final Retrofit retrofit;

    public RetrofitInitializer() {
        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public FirebaseService getFirebaseService() {return retrofit.create(FirebaseService.class);}

}
