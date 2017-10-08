package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class SystemActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private VGISystem vgiSystem;
    private Intent intent;
    private Bundle bundle;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();
        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        this.title = (TextView) findViewById(R.id.system_name);
        this.title.setText(this.vgiSystem.getName());

    }
}
