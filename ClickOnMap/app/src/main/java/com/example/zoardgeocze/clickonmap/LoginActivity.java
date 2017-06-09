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
    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView title = (TextView) findViewById(R.id.login_title);
        EditText user = (EditText) findViewById(R.id.login_user);
        EditText password = (EditText) findViewById(R.id.login_password);

        Intent intent = this.getIntent();
        this.bundle = intent.getExtras();

        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");

        title.setText(this.vgiSystem.getName());

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
        //intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivityForResult(intent,10);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            //setResult(1,data);
            Intent it = new Intent(getBaseContext(),MenuActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            it.putExtras(data.getExtras());

            //data.setClass(getBaseContext(),MenuActivity.class);
            //data.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(it);
            finish();
        }
    }
}
