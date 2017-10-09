package com.example.zoardgeocze.clickonmap.Services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ZoardGeocze on 08/10/17.
 */

public class Locationer implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    private final GoogleApiClient client;
    private final Context context;
    private LatLng coordenada;

    private TextView lat;
    private TextView lng;

    public GoogleApiClient getClient() {
        return client;
    }

    public Locationer(Context context, TextView lat, TextView lng) {

        this.client = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();

        this.client.connect();

        this.lat = lat;
        this.lng = lng;

        this.context = context;

    }

    @Override
    public void onLocationChanged(Location location) {
        this.coordenada = new LatLng(location.getLatitude(), location.getLongitude());
        this.lat.setText(String.valueOf(location.getLatitude()));
        this.lng.setText(String.valueOf(location.getLongitude()));
        Log.i("Locationer Changed","ENTROU!");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setSmallestDisplacement(50);
        request.setInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(this.client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
