package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.SystemTile;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;


/**
 * Created by ZoardGeocze on 28/04/17.
 */

public class LoginActivity extends AppCompatActivity {

    private VGISystem vgiSystem;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView title = (TextView) findViewById(R.id.login_title);
        EditText user = (EditText) findViewById(R.id.login_user);
        EditText password = (EditText) findViewById(R.id.login_password);

        this.intent = this.getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");

        title.setText(this.vgiSystem.getName());

        com.example.zoardgeocze.clickonmap.Design.AvenirBookTextView btn = (com.example.zoardgeocze.clickonmap.Design.AvenirBookTextView)
                findViewById(R.id.login_register_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });

    }

    public void backToMenu(View view) {
        finish();
    }

    public void verifyUser(View view) {
        finish();
    }

    public void frontToRegister(View view) {
        Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
        intent.putExtras(this.bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            //Não faz nada aqui

        }
        else if(resultCode == 1) {
            Bundle bundle = data.getExtras();
            this.intent.putExtras(bundle);
            setResult(1,this.intent);
            finish();
        }
    }
}
