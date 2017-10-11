package com.example.zoardgeocze.clickonmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Services.Locationer;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class SystemActivity extends AppCompatActivity {

    private SingletonFacadeController generalController;

    private Locationer location;

    private VGISystem vgiSystem;
    private Intent intent;
    private Bundle bundle;

    private TextView systemName;
    private TextView systemDescription;
    private TextView latitudeValue;
    private TextView longitudeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        this.intent = getIntent();
        this.bundle = this.intent.getExtras();
        this.vgiSystem = (VGISystem) bundle.getSerializable("vgiSystem");

        this.systemName = (TextView) findViewById(R.id.system_name);
        this.systemName.setText(this.vgiSystem.getName());

        this.systemDescription = (TextView) findViewById(R.id.system_description);
        this.systemDescription.setText(this.vgiSystem.getDescription());

        this.latitudeValue = (TextView) findViewById(R.id.latitude_value);
        this.longitudeValue = (TextView) findViewById(R.id.longitude_value);

        this.location = new Locationer(this,this.latitudeValue,this.longitudeValue);

    }

    public void goToMap(View view) {

        Intent mapIntent = new Intent(this, MapaActivity.class);
        mapIntent.putExtras(this.bundle);
        startActivity(mapIntent);

    }

    public void goToOptions(View view) {

    }

    public void collaborate(View view) {

        String latText = this.latitudeValue.getText().toString();
        String lngText = this.longitudeValue.getText().toString();

        if(!latText.equals("") && !lngText.equals("")) {

            Intent colabIntent = new Intent(this, ColabActivity.class);

            Double lat = Double.valueOf(latText);
            Double lng = Double.valueOf(lngText);

            colabIntent.putExtra("latitude",lat);
            colabIntent.putExtra("longitude",lng);
            colabIntent.putExtras(this.bundle);

            startActivity(colabIntent);
        } else {
            Toast.makeText(this,"Coordenadas não encontradas.\nAguardando localização do aparelho.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        this.location.getClient().disconnect();
        super.onDestroy();
    }
}














