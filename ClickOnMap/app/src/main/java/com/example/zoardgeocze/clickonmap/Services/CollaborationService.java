package com.example.zoardgeocze.clickonmap.Services;

import com.example.zoardgeocze.clickonmap.Model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by ZoardGeocze on 22/11/17.
 */

public interface CollaborationService {

    @FormUrlEncoded
    @POST("mobile/")
    Observable<String> sendCollaborationToServer(@Field("tag") String tag,
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
    Call<String> sendCollaborationImageToServer(@Part("tag") RequestBody tag,
                                        @Part("userId") RequestBody userId,
                                        @Part("collaborationId") RequestBody collaborationId,
                                        @Part("collaborationTitle") RequestBody collaborationTitle,
                                        @Part("collaborationDescription") RequestBody collaborationDescription,
                                        @Part MultipartBody.Part file);



}
