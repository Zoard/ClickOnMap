package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void backToLogin(View view) {
        //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        finish();
    }

    public void registerUser(View view) {
        Intent intent = getIntent();
        setResult(1,intent);
        finish();
    }
}
