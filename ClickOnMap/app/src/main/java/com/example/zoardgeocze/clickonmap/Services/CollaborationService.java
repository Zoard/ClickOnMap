package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by ZoardGeocze on 22/11/17.
 */

public interface CollaborationService {

    @FormUrlEncoded
    @POST("mobile/")
    Observable<String> sendCollaborationToServer(@Field("tag") String tag,
                                                 @Field("tagImage") String tagImage,
                                                 @Field("tagVideo") String tagVideo,
                                                 @Field("userId") String userId,
                                                 @Field("title") String title,
                                                 @Field("description") String description,
                                                 @Field("idCategory") int idCategory,
                                                 @Field("idType") int idType,
                                                 @Field("latitude") double latitude,
                                                 @Field("longitude") double longitude,
                                                 @Field("date") String date);

    @Multipart
    @POST("mobile/")
    Call<String> sendSingleMidiaCollaborationToServer(@Part("tag") RequestBody tag,
                                                @Part("tagImage") RequestBody tagImage,
                                                @Part("tagVideo") RequestBody tagVideo,
                                                @Part("userId") RequestBody userId,
                                                @Part("title") RequestBody title,
                                                @Part("description") RequestBody description,
                                                @Part("idCategory") RequestBody idCategory,
                                                @Part("idType") RequestBody idType,
                                                @Part("latitude") RequestBody latitude,
                                                @Part("longitude") RequestBody longitude,
                                                @Part("date") RequestBody date,
                                                @Part MultipartBody.Part file);

    @Multipart
    @POST("mobile/")
    Call<String> sendFullCollaborationToServer(@Part("tag") RequestBody tag,
                                               @Part("tagImage") RequestBody tagImage,
                                               @Part("tagVideo") RequestBody tagVideo,
                                               @Part("userId") RequestBody userId,
                                               @Part("title") RequestBody title,
                                               @Part("description") RequestBody description,
                                               @Part("idCategory") RequestBody idCategory,
                                               @Part("idType") RequestBody idType,
                                               @Part("latitude") RequestBody latitude,
                                               @Part("longitude") RequestBody longitude,
                                               @Part("date") RequestBody date,
                                               @Part MultipartBody.Part imageFile,
                                               @Part MultipartBody.Part videoFile);

    @GET("mobile")
    Observable<List<Collaboration>> getCollaborationsFromServer(@Query("tag") String tag);

    @GET
    Observable<ResponseBody> getMidia(@Url String url);



}
