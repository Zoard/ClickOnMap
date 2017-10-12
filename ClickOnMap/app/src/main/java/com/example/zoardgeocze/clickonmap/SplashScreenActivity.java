package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by ZoardGeocze on 11/10/17.
 */

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMenuActivity();
            }
        },3000);
    }

    private void showMenuActivity() {
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
