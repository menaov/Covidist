package com.example.covidist;

import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location location;
    private LatLng mHome;
    private int mRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        location = (Location) getIntent().getExtras().get("Location");
        mHome = (LatLng) getIntent().getExtras().get("HomeLocation");;
        mRange = (int) getIntent().getExtras().get("Range");;
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

        LatLng locationSet = new LatLng(location.getLatitude(), location.getLongitude());
        float results[] = new float[10];
        Location.distanceBetween(mHome.latitude,mHome.longitude,locationSet.latitude,locationSet.longitude, results);

        Marker markerCurrent = mMap.addMarker(new MarkerOptions().position(locationSet).title("Your Location"));
        markerCurrent.setSnippet("Distance From Home = " + results[0]);
        markerCurrent.showInfoWindow();
        markerCurrent.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.person_icon));

        Marker markerHome = mMap.addMarker(new MarkerOptions().position(mHome).title("Home"));
        markerHome.setSnippet("Radius = " + mRange);
        markerHome.showInfoWindow();
        markerHome.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.home_icon));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mHome));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mHome, 16));

        mMap.addCircle(new CircleOptions().center(mHome)
                .radius(mRange)
                .fillColor(R.color.radiusFill)
                .strokeColor(R.color.radiusStroke).strokeWidth(3).visible(true));

        mMap.addPolyline(new PolylineOptions().add(mHome,locationSet)
                .color(R.color.Bordux)
                .width(3)
                .visible(true));
    }
}
