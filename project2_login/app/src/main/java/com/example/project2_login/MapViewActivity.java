package com.example.project2_login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set default location (e.g., USC campus) and move the camera
        LatLng uscLocation = new LatLng(34.02116, -118.287132);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uscLocation, 15));

        // Add a sample event marker for testing
        LatLng sampleEventLocation = new LatLng(34.02116, -118.287132);
        mMap.addMarker(new MarkerOptions().position(sampleEventLocation).title("Sample Event"));

        // Set up map click listener to open CreateEventActivity on map click
        mMap.setOnMapClickListener(latLng -> openCreateEventActivity(latLng));
    }

    private void openCreateEventActivity(LatLng latLng) {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra("latitude", latLng.latitude);
        intent.putExtra("longitude", latLng.longitude);
        startActivity(intent);
    }
}
