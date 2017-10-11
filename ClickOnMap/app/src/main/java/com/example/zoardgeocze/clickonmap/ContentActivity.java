package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.Collaboration;

/**
 * Created by ZoardGeocze on 11/10/17.
 */

public class ContentActivity extends AppCompatActivity {

    private TextView title;
    private TextView description;
    private TextView category;
    private TextView subcategory;
    private ImageButton photo;
    private  ImageButton video;
    private ImageButton audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        Collaboration colab = (Collaboration) bundle.getSerializable("colab");

        if(colab != null) {

            this.title = (TextView) findViewById(R.id.content_title);
            this.title.setText(colab.getTitle());

            String textSize = colab.getTitle();

            if(textSize.length() <= 20) {
                this.title.setTextSize(30);
                this.title.setText(colab.getTitle());
            } else {
                this.title.setTextSize(24);
                this.title.setText(colab.getTitle());
            }

            this.description = (TextView) findViewById(R.id.content_description);
            this.description.setText(colab.getDescription());

            this.category = (TextView) findViewById(R.id.content_category);
            this.category.setText(colab.getCategoryName());

            this.subcategory = (TextView) findViewById(R.id.content_subcategory);
            this.subcategory.setText(colab.getSubcategoryName());

            this.photo = (ImageButton) findViewById(R.id.content_photo_btn);
            if(colab.getPhoto().equals("")) {
                this.photo.setBackground(getDrawable(R.drawable.photo_off));
            } else {
                this.photo.setBackground(getDrawable(R.drawable.photo_on));
            }

            this.video = (ImageButton) findViewById(R.id.content_video_btn);
            if(colab.getVideo().equals("")) {
                this.video.setBackground(getDrawable(R.drawable.video_off));
            } else {
                this.photo.setBackground(getDrawable(R.drawable.video_on));
            }

            this.audio = (ImageButton) findViewById(R.id.content_audio_btn);
            if(colab.getAudio().equals("")) {
                this.audio.setBackground(getDrawable(R.drawable.speaker_off));
            } else {
                this.audio.setBackground(getDrawable(R.drawable.speaker_on));
            }

        }



        //Toast.makeText(this,colab.getTitle(),Toast.LENGTH_SHORT).show();

    }

    public void backToMap(View view) {
        finish();
    }

    public void getPicture(View view) {

    }

    public void getVideo(View view) {

    }

    public void getAudio(View view) {

    }
}
