package com.example.zoardgeocze.clickonmap.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public class RetrofitInitializer {

    private final Retrofit retrofit;

    public RetrofitInitializer(Retrofit retrofit) {
        this.retrofit = new Retrofit
                .Builder()
                .baseUrl("http://ufvmaps.fornut.com.br/clickonmap/android_index.php")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
