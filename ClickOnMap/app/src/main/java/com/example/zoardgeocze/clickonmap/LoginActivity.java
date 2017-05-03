package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.SystemTile;


/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class LoginActivity extends AppCompatActivity {

    private SystemTile systemTile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView title = (TextView) findViewById(R.id.login_title);
        EditText user = (EditText) findViewById(R.id.login_user);
        EditText password = (EditText) findViewById(R.id.login_password);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        systemTile = (SystemTile) bundle.getSerializable("tile");

        title.setText(systemTile.getSystem().getName());

    }

    public void backToMenu(View view) {
        finish();
    }

    public void verifyUser(View view) {
        finish();
    }

    public void frontToRegister(View view) {
        Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
        startActivity(intent);
    }
}
