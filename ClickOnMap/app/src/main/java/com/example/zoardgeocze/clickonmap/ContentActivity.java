package com.example.zoardgeocze.clickonmap;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 11/10/17.
 */

public class ContentActivity extends AppCompatActivity {

    private static final String IMAGE_PATH = "imagensenviadas/";
    private static final String VIDEO_PATH = "arquivos/";

    private static final String ORIGIN = "origin";

    private Collaboration collab;
    private VGISystem vgiSystem;
    private String originActivity;

    private TextView title;
    private TextView description;
    private TextView category;
    private TextView subcategory;
    private ImageButton photo;
    private ImageButton video;
    private ImageButton audio;
    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        this.originActivity = intent.getStringExtra(ORIGIN);

        switch (this.originActivity) {

            case PendingCollabActivity.NAME:
                //getActionBar().set
                break;

        }

        this.collab = (Collaboration) bundle.getSerializable("collab");
        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        if(collab != null) {

            this.title = (TextView) findViewById(R.id.content_title);
            this.title.setText(collab.getTitle());

            String textSize = collab.getTitle();

            if(textSize.length() <= 20) {
                this.title.setText(collab.getTitle());
            } else {
                this.title.setTextSize(18);
                this.title.setText(collab.getTitle());
            }

            this.description = (TextView) findViewById(R.id.content_description);
            this.description.setText(collab.getDescription());

            this.category = (TextView) findViewById(R.id.content_category);
            this.category.setText(collab.getCategoryName());

            this.subcategory = (TextView) findViewById(R.id.content_subcategory);
            this.subcategory.setText(collab.getSubcategoryName());

            this.photo = (ImageButton) findViewById(R.id.content_photo_btn);
            if(collab.getPhoto().equals("")) {
                this.photo.setBackground(getDrawable(R.drawable.photo_off));
            } else {
                this.photo.setBackground(getDrawable(R.drawable.photo_on));
            }

            this.video = (ImageButton) findViewById(R.id.content_video_btn);
            if(collab.getVideo().equals("")) {
                this.video.setBackground(getDrawable(R.drawable.video_off));
            } else {
                this.video.setBackground(getDrawable(R.drawable.video_on));
            }

            this.audio = (ImageButton) findViewById(R.id.content_audio_btn);
            if(collab.getAudio().equals("")) {
                this.audio.setBackground(getDrawable(R.drawable.speaker_off));
            } else {
                this.audio.setBackground(getDrawable(R.drawable.speaker_on));
            }

            this.userName = (TextView) findViewById(R.id.content_collaborator_name);
            this.userName.setText(collab.getUserId());

        }

    }

    public void backToMap(View view) {
        finish();
    }

    public void getPicture(View view) {

        if(!this.collab.getPhoto().equals("")) {

            Toast.makeText(this,collab.getPhoto(),Toast.LENGTH_SHORT).show();

            File file = null;

            switch (originActivity) {
                case MapaActivity.NAME:

                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    file = new File(storageDir,this.collab.getPhoto());

                    break;

                case PendingCollabActivity.NAME:

                    file = new File(this.collab.getPhoto());

                    break;
            }

            Toast.makeText(this,collab.getPhoto(),Toast.LENGTH_SHORT).show();

            if (file != null && file.exists()) {
                Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    Log.i("START_ACTIVITY", this.collab.getPhoto());
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                String base_url = this.vgiSystem.getAddress() + "/";
                downloadPictureFromServer(base_url);
            }
        } else {
            Toast.makeText(this,"Nenhuma foto disponível ;(",Toast.LENGTH_SHORT).show();
        }



    }

    private void downloadPictureFromServer(String base_url) {

        final ProgressDialog mProgressDialog = new ProgressDialog(ContentActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Carregando Imagem...");
        mProgressDialog.show();

        new RetrofitClientInitializer(base_url)
                .getCollaborationService()
                .getMidia(IMAGE_PATH + this.collab.getPhoto())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.i( "onNext_CONTENT: ",responseBody.toString());
                        if(createImageFile(responseBody)) {
                            visualizePicture();
                        }
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }
                });

    }

    private boolean createImageFile(ResponseBody body) {
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File picture = new File(storageDir,this.collab.getPhoto());

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(picture);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void visualizePicture() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir,this.collab.getPhoto());
        if (file.exists()) {
            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Log.i("START_ACTIVITY", this.collab.getPhoto());
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void getVideo(View view) {

        if(!this.collab.getVideo().equals("")) {

            Toast.makeText(this,collab.getVideo(),Toast.LENGTH_SHORT).show();

            File file = null;

            switch (originActivity) {

                case MapaActivity.NAME:

                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                    file = new File(storageDir,this.collab.getVideo());

                    break;

                case PendingCollabActivity.NAME:

                    file = new File(this.collab.getVideo());

                    break;

            }

            if (file != null && file.exists()) {
                Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "video/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                String base_url = this.vgiSystem.getAddress() + "/";
                downloadVideoFromServer(base_url);
            }


        } else {
            Toast.makeText(this,"Nenhum vídeo disponível ;(",Toast.LENGTH_SHORT).show();
        }

    }

    private void downloadVideoFromServer(String base_url) {

        final ProgressDialog mProgressDialog = new ProgressDialog(ContentActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Carregando Vídeo...");
        mProgressDialog.show();

        new RetrofitClientInitializer(base_url)
                .getCollaborationService()
                .getMidia(VIDEO_PATH + this.collab.getVideo())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.i( "onNext_CONTENT: ",responseBody.toString());
                        if(createVideoFile(responseBody)) {
                            visualizeVideo();
                        }
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }
                });

    }


    private boolean createVideoFile(ResponseBody body) {
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            File video = new File(storageDir,this.collab.getVideo());

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(video);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void visualizeVideo() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File file = new File(storageDir,this.collab.getVideo());
        if (file.exists()) {
            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "video/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Log.i("START_ACTIVITY", this.collab.getVideo());
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAudio(View view) {
        Toast.makeText(this,"Nenhum áudio disponível ;(",Toast.LENGTH_SHORT).show();
    }
}
