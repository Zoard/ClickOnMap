package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class ColabActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private VGISystem vgiSystem;

    private Intent intent;
    private Bundle bundle;

    private TextView systemName;

    private EditText title;
    private EditText description;

    private ImageButton photoBtn;
    private ImageButton videoBtn;
    private ImageButton audioBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colab);

        this.intent = getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        this.systemName = (TextView) findViewById(R.id.colab_system_name);
        this.systemName.setText(this.vgiSystem.getName());

        this.title = (EditText) findViewById(R.id.colab_title);
        this.description = (EditText) findViewById(R.id.colab_description);

        this.photoBtn = (ImageButton) findViewById(R.id.colab_photo_btn);
        this.videoBtn = (ImageButton) findViewById(R.id.colab_video_btn);
        this.audioBtn = (ImageButton) findViewById(R.id.colab_audio_btn);

    }

    public void closeColab(View view) {
        finish();
    }

    public void takePicture(View view) {
        this.photoBtn.setBackground(getDrawable(R.drawable.photo_on));
    }

    public void takeVideo(View view) {
        this.videoBtn.setBackground(getDrawable(R.drawable.video_on));
    }

    public void takeAudio(View view) {
        this.audioBtn.setBackground(getDrawable(R.drawable.speaker_on));
    }


}




















