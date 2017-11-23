package com.example.zoardgeocze.clickonmap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.EventCategory;
import com.example.zoardgeocze.clickonmap.Model.EventType;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Retrofit.RetrofitClientInitializer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class ColabActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final int REQUEST_TAKE_PHOTO = 100;
    public static final int REQUEST_VIDEO_CAPTURE = 101;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private SingletonFacadeController generalController;

    private Collaboration collaboration;

    private VGISystem vgiSystem;

    private Double latitude;
    private Double longitude;

    private EventCategory choosedCategory;
    private EventType choosedType;

    private Intent intent;
    private Bundle bundle;

    private EditText title;
    private EditText description;

    private Spinner categorySpinner;
    private Spinner subcategorySpinner;

    private ImageButton photoBtn;
    private ImageButton videoBtn;
    private ImageButton audioBtn;

    private Button collaborate;

    private Uri myPhoto;
    private String currentPhotoPath = "";
    private String currentVideoPath = "";
    private String currentAudioPath = "";

    private static String ID;
    private static Context myContext;

    public VGISystem getVgiSystem() {
        return vgiSystem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colab);

        this.title = (EditText) findViewById(R.id.colab_title);
        this.description = (EditText) findViewById(R.id.colab_description);

        this.generalController = SingletonFacadeController.getInstance();


        this.intent = getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        this.categorySpinner = (Spinner) findViewById(R.id.colab_category);
        this.categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<EventCategory> categoryAdapter = new ArrayAdapter<EventCategory>(this,R.layout.spinner_item,this.vgiSystem.getCategory());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.categorySpinner.setAdapter(categoryAdapter);

        this.subcategorySpinner = (Spinner) findViewById(R.id.colab_subcategory);
        this.subcategorySpinner.setOnItemSelectedListener(this);

        ID = this.vgiSystem.getAddress();
        myContext = this.getBaseContext();

        this.latitude = intent.getDoubleExtra("latitude",0);
        this.longitude = intent.getDoubleExtra("longitude",0);

        this.photoBtn = (ImageButton) findViewById(R.id.colab_photo_btn);
        this.videoBtn = (ImageButton) findViewById(R.id.colab_video_btn);
        this.audioBtn = (ImageButton) findViewById(R.id.colab_audio_btn);

        this.collaborate = (Button) findViewById(R.id.colab_btn);
        this.collaborate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().equals("") &&
                        !choosedCategory.getDescription().equals("") &&
                        !choosedType.getDescription().equals("")) {

                    String titleText = title.getText().toString();
                    String descriptionText = description.getText().toString();
                    String timeStamp = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.US).format(new Date());
                    String userId = generalController.getUserId(vgiSystem.getAddress());

                    collaboration = new Collaboration(userId,titleText,descriptionText, timeStamp, choosedCategory.getId(),
                            choosedCategory.getDescription(), choosedType.getId(),
                            choosedType.getDescription(),
                            currentPhotoPath, currentVideoPath,currentAudioPath,
                            latitude, longitude);

                    String base_url = vgiSystem.getAddress() + "/";
                    if(currentPhotoPath.equals("") && currentVideoPath.equals("")) {
                        sendCollaborationToServer(base_url,collaboration);
                    } else {
                        sendMidiaCollaboration(base_url,collaboration);
                    }


                    //generalController.registerPendingCollaborations(collaboration,vgiSystem.getAddress());

                    Toast.makeText(getBaseContext(),"Colaboração feita com sucesso ;)", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getBaseContext(),"Os campos com * são obrigatórios", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    //Colaboração é anulada
    public void closeColab(View view) {
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        this.choosedCategory = this.vgiSystem.getCategory().get(position);

        Log.i("ON_ITEM_SELECTED: ", String.valueOf(position) + " " + this.choosedCategory);

        this.subcategorySpinner = (Spinner) findViewById(R.id.colab_subcategory);

        ArrayAdapter<EventType> eventTypeAdapter = new ArrayAdapter<EventType>(this,R.layout.spinner_item,this.choosedCategory.getEventTypes());
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.subcategorySpinner.setAdapter(eventTypeAdapter);

        this.subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosedType = choosedCategory.getEventTypes().get(position);
                //Toast.makeText(getBaseContext(),subcategories.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Tira a foto pedindo uma resposta da câmera
    public void takePicture(View view) {

        Intent takePictureIntent = new Intent(this,CameraActivity.class);
        takePictureIntent.putExtras(bundle);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

    }

    //Filma uma cena pedindo resposta da câmera
    public void takeVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the video should go
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("CAMERA_COLAB", "EXCEPTION");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID,
                        videoFile);
                Log.i("FILE_PROVIDER", videoURI.toString());
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                takeVideoIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, 0);
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }


        }
    }

    public void takeAudio(View view) {
        //this.audioBtn.setBackground(getDrawable(R.drawable.speaker_on));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,"Imagem armazenada com sucesso ;)",Toast.LENGTH_SHORT).show();
                this.photoBtn.setBackground(getDrawable(R.drawable.photo_on));
                Bundle bundle = data.getExtras();
                this.currentPhotoPath = (String) bundle.getSerializable("photoPath");
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Tirar foto cancelado",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_VIDEO_CAPTURE ) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Vídeo armazenado com sucesso ;)", Toast.LENGTH_SHORT).show();
                this.videoBtn.setBackground(getDrawable(R.drawable.video_on));
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Filmagem cancelada",Toast.LENGTH_SHORT).show();
                this.currentVideoPath = "";
            }
        }
    }

    //TODO: Separar essas funções criando uma Classe que realiza as tarefas de produção de Mídia
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());
        //String imageFileName = "IMG_"+ this.vgiSystem.getAdress() + "_" + timeStamp;
        String systemName = this.vgiSystem.getName();
        String imageFileName = "IMG_" + systemName + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //File image = File.createTempFile(
        //        imageFileName,  /* prefix */
        //        ".jpg",         /* suffix */
        //        storageDir      /* directory */
        //);
        //File image = File.createTempFile(imageFileName,".jpg",storageDir);

        File image = new File(storageDir,imageFileName.concat(".jpg"));

        // Save a file: path for use with ACTION_VIEW intents
        this.currentPhotoPath = image.getAbsolutePath();

        Log.i("CURRENT_PHOTO_PATH", this.currentPhotoPath);

        return image;
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());
        String systemName = this.vgiSystem.getName();
        String videoFileName = "VID_" + systemName + "_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);

        File video = new File(storageDir,videoFileName.concat(".mp4"));

        // Save a file: path for use with ACTION_VIEW intents
        this.currentVideoPath = video.getAbsolutePath();

        Log.i("CURRENT_VIDEO_PATH", this.currentVideoPath);

        return video;
    }

    //Na Versão DEMO para o GEOINFO, irei salvar as colaborações localmente apenas
    //TODO: Aqui a colaboração deverá ser enviada para o Servidor do Sistema, caso contrário, colaboração é salva localmente
    public void sendColaboration(View view) {



    }

    private void sendCollaborationToServer(String base_url,final Collaboration collaboration) {

        final ProgressDialog mProgressDialog = new ProgressDialog(ColabActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Enviando Colaboração...");
        mProgressDialog.show();

        new RetrofitClientInitializer(base_url)
                .getCollaborationService()
                .sendCollaborationToServer("collaboration","N","N",
                        collaboration.getUserId(), collaboration.getTitle(), collaboration.getDescription(),
                        collaboration.getCategoryId(), collaboration.getSubcategoryId(), collaboration.getLatitude(),
                        collaboration.getLongitude(), collaboration.getCollaborationDate())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i("onNext_COLLABORATION: ", currentPhotoPath);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                        //TODO: Persistir no Banco Local
                        finish();
                    }

                    @Override
                    public void onNext(String s) {
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                        finish();
                    }
                });
    }

    private void sendMidiaCollaboration(String base_url, Collaboration collaboration) {

        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "collaboration");
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),collaboration.getUserId());
        RequestBody collabTitle = RequestBody.create(MediaType.parse("text/plain"),collaboration.getTitle());
        RequestBody collabDescript = RequestBody.create(MediaType.parse("text/plain"),collaboration.getDescription());
        RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getCategoryId()));
        RequestBody typeId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getSubcategoryId()));
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getLatitude()));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(collaboration.getLongitude()));
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),collaboration.getCollaborationDate());

        if(!currentPhotoPath.equals("") && !currentVideoPath.equals("")) {

            //Imagem
            File imageFile = new File(currentPhotoPath);
            Log.i("sendCollaborationImage",imageFile.getAbsolutePath());
            RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part imageBody =
                    MultipartBody.Part.createFormData("image_file", imageFile.getName(), requestImageFile);

            //Video
            File videoFile = new File(currentVideoPath);
            Log.i("sendCollaborationVideo",videoFile.getAbsolutePath());
            RequestBody requestVideoFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part videoBody =
                    MultipartBody.Part.createFormData("video_file", videoFile.getName(), requestVideoFile);

            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "Y");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "Y");

            final ProgressDialog mProgressDialog = new ProgressDialog(ColabActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<String> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendFullCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,imageBody,videoBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });

        }

        else if(!currentPhotoPath.equals("")) {
            //Imagem
            File imageFile = new File(currentPhotoPath);
            Log.i("sendCollaborationImage",imageFile.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image_file", imageFile.getName(), requestFile);
            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "Y");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "N");

            final ProgressDialog mProgressDialog = new ProgressDialog(ColabActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<String> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendSingleMidiaCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });
        }

        else {

            //Video
            File videoFile = new File(currentVideoPath);
            Log.i("sendCollaborationVideo",videoFile.getAbsolutePath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("video_file", videoFile.getName(), requestFile);
            RequestBody tagImage = RequestBody.create(MediaType.parse("text/plain"), "N");
            RequestBody tagVideo = RequestBody.create(MediaType.parse("text/plain"), "Y");

            final ProgressDialog mProgressDialog = new ProgressDialog(ColabActivity.this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Enviando Colaboração...");
            mProgressDialog.show();

            Call<String> call = new RetrofitClientInitializer(base_url)
                    .getCollaborationService()
                    .sendSingleMidiaCollaborationToServer(tag,tagImage,tagVideo,userId,collabTitle,
                            collabDescript,categoryId,typeId,latitude,longitude,date,body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    finish();
                    Log.i("onResponse_IMAGE: ",t.getMessage());
                }
            });
        }

    }


}




















