package com.example.covidist;

import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private Location mLocation;
    private LatLng mHome;
    private int mRange;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocation = (Location) getIntent().getExtras().get("Location");
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
        initializeMap();
    }

    private void initializeMap() {
        mMap.clear();
        getLocation();
        LatLng locationSet = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        float results[] = new float[10];
        Location.distanceBetween(mHome.latitude,mHome.longitude,locationSet.latitude,locationSet.longitude, results);

        Marker markerCurrent = mMap.addMarker(new MarkerOptions().position(locationSet).title("Your Location"));
        markerCurrent.setSnippet("Distance From Home = " + results[0]);
        markerCurrent.showInfoWindow();
        markerCurrent.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.person_marker));

        Marker markerHome = mMap.addMarker(new MarkerOptions().position(mHome).title("Home"));
        markerHome.setSnippet("Radius = " + mRange);
        markerHome.showInfoWindow();
        markerHome.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.home_marker));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(locationSet));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationSet, 16));

        mMap.addCircle(new CircleOptions().center(mHome)
                .radius(mRange)
                .fillColor(R.color.radiusFill)
                .strokeColor(R.color.radiusStroke).strokeWidth(3).visible(true));

        mMap.addPolyline(new PolylineOptions().add(mHome,locationSet)
                .color(R.color.Bordux)
                .width(3)
                .visible(true));
    }

    public void getLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener)this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mLocation == null){
            mLocation = location;
        }
        if(location != null) {
            if (mLocation.distanceTo(location) >= 1) {
                mLocation = location;
                initializeMap();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Map.this, R.string.giveLocationToast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
    }
}
