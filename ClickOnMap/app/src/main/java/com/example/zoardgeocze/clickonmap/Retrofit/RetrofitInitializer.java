package com.example.zoardgeocze.clickonmap.Retrofit;

import com.example.zoardgeocze.clickonmap.Services.FirebaseService;
import com.example.zoardgeocze.clickonmap.Services.VGISystemService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ZoardGeocze on 01/08/2017.
 */

public class RetrofitInitializer {

    private static final String BASE_URL = "http://ufvmaps.fornut.com.br/clickonmap/";
    private final Retrofit retrofit;

    public RetrofitInitializer() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        this.retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public FirebaseService getFirebaseService() {return retrofit.create(FirebaseService.class);}

    public VGISystemService getSystemService() {return retrofit.create(VGISystemService.class);}

}
