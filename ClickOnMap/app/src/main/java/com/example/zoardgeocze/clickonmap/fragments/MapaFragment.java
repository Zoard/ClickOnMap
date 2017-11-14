package com.example.zoardgeocze.clickonmap.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zoardgeocze.clickonmap.ContentActivity;
import com.example.zoardgeocze.clickonmap.Model.Collaboration;
import com.example.zoardgeocze.clickonmap.Model.VGISystem;
import com.example.zoardgeocze.clickonmap.Singleton.SingletonFacadeController;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZoardGeocze on 10/10/17.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener {

    private SingletonFacadeController generalController;

    private GoogleMap map;

    private Collaboration collaboration;

    private Bundle bundle;

    private VGISystem vgiSystem;

    private List<Collaboration> collaborations = new ArrayList<>();


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.bundle = getArguments();
        this.vgiSystem = (VGISystem) this.bundle.getSerializable("vgiSystem");
        this.generalController = SingletonFacadeController.getInstance();
        this.collaboration = new Collaboration();
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;
        this.map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.map.setMyLocationEnabled(true);

        this.collaborations = this.generalController.getCollaborations(vgiSystem.getAddress());
        Log.i("DEPOIS_DO_BD", String.valueOf(this.collaborations.size()));

        for(Collaboration colab: this.collaborations) {
            LatLng ll = new LatLng(colab.getLatitude(),colab.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(ll);
            String userName = this.generalController.getUserName(colab.getUserId());
            markerOptions.title(colab.getTitle());
            markerOptions.snippet("Categoria: " + colab.getCategoryName() + " - " +"Usuário: " + userName);

            Marker marker = this.map.addMarker(markerOptions);
            marker.setTag(colab);
        }

        this.map.setOnInfoWindowClickListener(this);
        this.map.setInfoWindowAdapter(this);
        this.map.setOnMarkerClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        this.collaboration = (Collaboration) marker.getTag();

        Intent intent = new Intent(this.getContext(), ContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("colab",this.collaboration);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
}














