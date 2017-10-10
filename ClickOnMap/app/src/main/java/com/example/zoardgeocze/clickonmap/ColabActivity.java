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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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

public class ColabActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private SingletonFacadeController generalController;

    private Collaboration collaboration;

    private VGISystem vgiSystem;

    private Double latitude;
    private Double longitude;

    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> subcategories = new ArrayList<>();

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
    private String currentPhotoPath;

    private static String ID;
    private static Context myContext;

    public VGISystem getVgiSystem() {
        return vgiSystem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colab);

        this.generalController = SingletonFacadeController.getInstance();

        this.intent = getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        ID = this.vgiSystem.getAdress();
        myContext = this.getBaseContext();

        this.latitude = intent.getDoubleExtra("latitude",0);
        this.longitude = intent.getDoubleExtra("longitude",0);

        this.systemName = (TextView) findViewById(R.id.colab_system_name);
        this.systemName.setText(this.vgiSystem.getName());

        this.title = (EditText) findViewById(R.id.colab_title);
        this.description = (EditText) findViewById(R.id.colab_description);

        this.categorySpinner = (Spinner) findViewById(R.id.colab_category);

        this.photoBtn = (ImageButton) findViewById(R.id.colab_photo_btn);
        this.videoBtn = (ImageButton) findViewById(R.id.colab_video_btn);
        this.audioBtn = (ImageButton) findViewById(R.id.colab_audio_btn);

    }

    //Colaboração é anulada
    public void closeColab(View view) {
        finish();
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

        //this.photoBtn.setBackground(getDrawable(R.drawable.photo_on));
    }


    public void takeVideo(View view) {
        //this.videoBtn.setBackground(getDrawable(R.drawable.video_on));
    }

    public void takeAudio(View view) {
        //this.audioBtn.setBackground(getDrawable(R.drawable.speaker_on));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.photoBtn.setBackground(getDrawable(R.drawable.photo_on));
        }
    }

    //TODO: Aqui a colaboração deverá ser enviada para o Servidor do Sistema, caso contrário, colaboração é salva.
    public void sendColaboration(View view) {


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        Log.i("CURRENT_PHOTO", storageDir.toString());
        //File image = File.createTempFile(
        //        imageFileName,  /* prefix */
        //        ".jpg",         /* suffix */
        //        storageDir      /* directory */
        //);

        //File image = File.createTempFile(imageFileName,".jpg",storageDir);
        File image = new File(storageDir,imageFileName.concat(".jpg"));

        // Save a file: path for use with ACTION_VIEW intents
        this.currentPhotoPath = image.getAbsolutePath();


        return image;
    }

}




















