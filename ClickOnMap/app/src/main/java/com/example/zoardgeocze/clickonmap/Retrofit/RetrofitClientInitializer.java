package com.example.zoardgeocze.clickonmap.Retrofit;

import com.example.zoardgeocze.clickonmap.Services.UserService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ZoardGeocze on 09/08/2017.
 */

public class RetrofitClientInitializer {

    private final Retrofit retrofit;

    public RetrofitClientInitializer(String base_url) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(base_url)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public UserService getUserService() {return retrofit.create(UserService.class);}

}
