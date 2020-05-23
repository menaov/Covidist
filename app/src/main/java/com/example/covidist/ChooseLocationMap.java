package com.example.covidist;

import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class ChooseLocationMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Location location;
    private LatLng currentLocation;
    private Marker currentChoose = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.chooseMap);
        mapFragment.getMapAsync(this);

        location = (Location) getIntent().getExtras().get("Location");
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationButtonClickListener(this);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        mMap.clear();
        currentChoose = mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        showSnackbar(latLng);
    }

    private void showSnackbar(final LatLng latLng) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.chooseMap),
                R.string.chooseLocationMsg, Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.chooseSnackBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("ChosenLocation", latLng);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        mySnackbar.show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(currentLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
        showSnackbar(currentLocation);
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showSnackbar(marker.getPosition());
        return false;
    }
}
