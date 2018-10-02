package com.example.zoardgeocze.clickonmap.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.PendingCollabActivity;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 14/02/2018.
 */

public class CollaborationSender {

    private SingletonFacadeController generalController;
    private Context context;

    final private Collaboration collaboration;
    final private String systemAddress;
    private boolean pending;

    public CollaborationSender(Collaboration collaboration, String systemAddress, Context context, boolean pending) {

        this.generalController = SingletonFacadeController.getInstance();

        this.collaboration = collaboration;
        this.systemAddress = systemAddress;
        this.context = context;
        this.pending = pending;
    }

    public void sendCollaborationToServer() {

        final ProgressDialog mProgressDialog = new ProgressDialog(this.context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Enviando Colaboração...");
        mProgressDialog.show();

        String base_url = systemAddress + "/";

        new RetrofitClientInitializer(base_url)
                .getCollaborationService()
                .sendCollaborationToServer("collaboration","N","N",
                        collaboration.getUserId(), collaboration.getTitle(), collaboration.getDescription(),
                        collaboration.getCategoryId(), collaboration.getSubcategoryId(), collaboration.getLatitude(),
                        collaboration.getLongitude(), collaboration.getCollaborationDate())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        //Log.i("onNext_COLLABORATION: ", currentPhotoPath);
                    }

                    @Override
                    public void onError(Throwable e) {

                        pendingCollaboration();

                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }


                        Log.i("onError_COLLABORATION: ", e.getMessage());

                    }

                    @Override
                    public void onNext(Void aVoid) {

                        success();

                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }

                    }
                });

    }

    public void sendMidiaCollaboration() {

        String base_url = systemAddress + "/";

        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "collaboration");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),collaboration.getUserId());
        RequestBody collabTitle = RequestBody.create(MediaType.parse("text/plain"),collaboration.getTitle());
        RequestBody collabDescript = RequestBody.create(MediaType.parse("text/plain"),collaboration.getDescription());
        RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getCategoryId()));
        RequestBody typeId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getSubcategoryId()));
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getLatitude()));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getLongitude()));
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),collaboration.getCollaborationDate());

        if (!collaboration.getPhoto().equals("") && !collaboration.getVideo().equals("")) {

            //Imagem
            File imageFile = new File(collaboration.getPhoto());
            Log.i("sendCollaborationImage",imageFile.getAbsolutePath());
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imageBody =
                    MultipartBody.Part.createFormData("image_file", imageFile.getName(), requestImageFile);

            //Video
            File videoFile = new File(collaboration.getVideo());
            Log.i("sendCollaborationVideo",videoFile.getAbsolutePath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part videoBody =
                    MultipartBody.Part.createFormData("video_file", videoFile.getName(), requestVideoFile);

            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "Y");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "Y");

            final ProgressDialog mProgressDialog = new ProgressDialog(this.context);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<Void> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendFullCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,imageBody,videoBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    success();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    pendingCollaboration();

                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });

        }

        else if (!collaboration.getPhoto().equals("")) {
            //Imagem
            File imageFile = new File(this.collaboration.getPhoto());
            Log.i("sendCollaborationImage",imageFile.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image_file", imageFile.getName(), requestFile);
            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "Y");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "N");

            final ProgressDialog mProgressDialog = new ProgressDialog(this.context);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<Void> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendSingleMidiaCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    success();

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    pendingCollaboration();

                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });
        }

        else {

            //Video
            File videoFile = new File(this.collaboration.getVideo());
            Log.i("sendCollaborationVideo",videoFile.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("video_file", videoFile.getName(), requestFile);
            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "N");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "Y");

            final ProgressDialog mProgressDialog = new ProgressDialog(this.context);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<Void> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendSingleMidiaCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,body);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    success();

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }

                    pendingCollaboration();

                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });
        }

    }

    private void success() {
        Toast.makeText(this.context,
                "Colaboração feita com sucesso!",
                Toast.LENGTH_LONG).show();

        if (!pending) {
            ((Activity)this.context).finish();
        } else {
            ((PendingCollabActivity)this.context).deletePendingCollaboration(((PendingCollabActivity) this.context).getPosition());
        }

    }

    private void pendingCollaboration() {

        Toast.makeText(this.context,
                "Não foi possível obter uma conexão com o servidor. Sua Colaboração está pendente.",
                Toast.LENGTH_LONG).show();

        if (!pending) {
            this.generalController.registerPendingCollaborations(this.collaboration,this.systemAddress);
            ((Activity)this.context).finish();
        }

    }



}
