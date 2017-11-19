package com.example.zoardgeocze.clickonmap;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> subcategories = new ArrayList<>();

    private int choosedCategoryId;
    private String choosedCategory;
    private int choosedSubcategoryId;
    private String choosedSubcategory;

    private Intent intent;
    private Bundle bundle;

    private TextView systemName;

    private EditText title;
    private EditText description;

    private Spinner categorySpinner;
    private Spinner subcategorySpinner;

    private ImageButton photoBtn;
    private ImageButton videoBtn;
    private ImageButton audioBtn;

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

        //Função Temporária - Demo GEOINFO
        if(this.vgiSystem != null) {
            this.categories = this.generalController.getCategoriesFromSystem(this.vgiSystem.getAddress());
            this.categories.add(0,"--");
            //this.subcategories = this.generalController.getSubcategoriesFromSystem(this.categories);
        }

        this.categorySpinner = (Spinner) findViewById(R.id.colab_category);
        this.categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,this.categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.categorySpinner.setAdapter(categoryAdapter);

        this.subcategorySpinner = (Spinner) findViewById(R.id.colab_subcategory);
        this.subcategorySpinner.setOnItemSelectedListener(this);

        ID = this.vgiSystem.getAddress();
        myContext = this.getBaseContext();

        this.latitude = intent.getDoubleExtra("latitude",0);
        this.longitude = intent.getDoubleExtra("longitude",0);

        this.systemName = (TextView) findViewById(R.id.colab_system_name);
        this.systemName.setText(this.vgiSystem.getName());

        this.photoBtn = (ImageButton) findViewById(R.id.colab_photo_btn);
        this.videoBtn = (ImageButton) findViewById(R.id.colab_video_btn);
        this.audioBtn = (ImageButton) findViewById(R.id.colab_audio_btn);

    }

    //Colaboração é anulada
    public void closeColab(View view) {
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        this.choosedCategory = this.categories.get(position);
        this.choosedCategoryId = position;

        Log.i("ON_ITEM_SELECTED: ", String.valueOf(position) + " " + this.choosedCategory);

        this.subcategories = this.generalController.getTypesFromSystem(this.choosedCategoryId);

        this.subcategorySpinner = (Spinner) findViewById(R.id.colab_subcategory);

        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,this.subcategories);
        subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        this.subcategorySpinner.setAdapter(subcategoryAdapter);

        this.subcategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosedSubcategory = subcategories.get(position);
                choosedSubcategoryId = position;
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

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("CAMERA_COLAB", "EXCEPTION");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID,
                        photoFile);
                Log.i("FILE_PROVIDER", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 320);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

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
            }
        }
    }

    //TODO: Separar essas funções criando uma Classe que realiza as tarefas de produção de Mídia
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());
        //String imageFileName = "IMG_"+ this.vgiSystem.getAdress() + "_" + timeStamp;
        String systemAdress = this.vgiSystem.getAddress();
        String imageFileName = "IMG_" + timeStamp;
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
        String videoFileName = "VID_" + timeStamp;
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

        if(!this.title.getText().toString().equals("") && !this.choosedCategory.equals("") && !this.choosedSubcategory.equals("")) {

            String titleText = this.title.getText().toString();
            String descriptionText = this.description.getText().toString();
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.US).format(new Date());
            String userId = this.generalController.getUserId(this.vgiSystem.getAddress());

            this.collaboration = new Collaboration(userId,titleText,descriptionText, timeStamp, this.choosedCategoryId,
                                                    this.choosedCategory, this.choosedSubcategoryId, this.choosedSubcategory,
                                                    this.currentPhotoPath, this.currentVideoPath,this.currentAudioPath,
                                                    this.latitude, this.longitude);

            this.generalController.registerPendingCollaborations(this.collaboration,this.vgiSystem.getAddress());

            Toast.makeText(this,"Colaboração feita com sucesso ;)", Toast.LENGTH_LONG).show();

            finish();

        } else {
            Toast.makeText(this,"Os campos com * são obrigatórios", Toast.LENGTH_SHORT).show();
        }

    }


}




















