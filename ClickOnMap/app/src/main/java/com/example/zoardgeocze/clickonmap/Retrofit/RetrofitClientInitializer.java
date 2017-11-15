package com.example.zoardgeocze.clickonmap.Retrofit;

import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Services.UserService;
import com.example.zoardgeocze.clickonmap.Services.VGISystemService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client.build())
                .build();
    }

    public UserService getUserService() {return retrofit.create(UserService.class);}
    public VGISystemService getSystemService() {return retrofit.create(VGISystemService.class);}

}
