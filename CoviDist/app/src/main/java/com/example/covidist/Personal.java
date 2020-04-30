package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Personal extends AppCompatActivity implements View.OnClickListener, LocationListener {
    private static final int PERMISSION_REQUEST_CODE = 99;
    private Button currentLocation;
    private Button setHomeBtn;
    private Button updateDistanceBtn;
    private Location mLocation;
    private LocationManager mLocationManager;
    private FirebaseUser mFirebaseUser;
    private FirebaseAssistant mFirebaseAssistant;
    private List<DataManager> mDataManagerUsers;
    private DataManager currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        currentLocation = findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(this);
        setHomeBtn = findViewById(R.id.setHomeBtn);
        setHomeBtn.setOnClickListener(this);
        updateDistanceBtn = findViewById(R.id.updateDistanceBtn);
        updateDistanceBtn.setOnClickListener(this);
        CheckPermission();
        initializeData();
    }

    private void initializeData() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAssistant = FirebaseAssistant.getInstance();
        mDataManagerUsers = mFirebaseAssistant.getmUsers();
        currentUserData = null;

        for(DataManager u : mDataManagerUsers){
            if(u.getmEmail() == mFirebaseUser.getEmail()){
                currentUserData = u;
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.currentLocation:
                currentLocationClick();
                break;
            case R.id.setHomeBtn:
                setHomeClick();
                break;
            case R.id.updateDistanceBtn:
                updateDistanceClick();
                break;
        }
    }

    private void updateDistanceClick() {
        Intent intent = new Intent(Personal.this, UpdateDistance.class);
        startActivity(intent);
    }

    private void setHomeClick() {
        Intent intent = new Intent(Personal.this, HomeLocation.class);
        startActivity(intent);
    }

    public void currentLocationClick() {
        if(checkIfLocationEnabled()){
            LatLng home = getHomeFromFirebase();
            int range = getRangeFromFirebase();
            Intent intent = new Intent(Personal.this, Map.class);
            intent.putExtra("Location", mLocation);
            intent.putExtra("HomeLocation", home);
            intent.putExtra("Range", range);
            startActivity(intent);
        }
        else{
            Toast.makeText(Personal.this, R.string.giveLocationToast, Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Personal.this, R.string.giveLocationToast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        String allowPermissions = getResources().getString(R.string.allowPermissions);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.PermissionGranted, Toast.LENGTH_SHORT).show();
                    CheckPermission();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.PermissionDenied, Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {
                            showMessageOKCancel(allowPermissions,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                CheckPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Personal.this)
                .setMessage(message)
                .setPositiveButton(R.string.Ok, okListener)
                .setNegativeButton(R.string.Cancel, null)
                .create()
                .show();
    }

    private boolean checkIfLocationEnabled(){
        boolean gps_enabled = false;

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled) {
            new AlertDialog.Builder(Personal.this)
                    .setMessage(R.string.giveLocationToast)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel, null)
                    .show();
        }
        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        return gps_enabled;
    }

    private LatLng getHomeFromFirebase() {
        initializeData();
        double latitude = (double)((HashMap<String,Object>)currentUserData.getmHomeLocation()).get("latitude");
        double longitude = (double)((HashMap<String,Object>)currentUserData.getmHomeLocation()).get("longitude");
        LatLng toReturn = new LatLng(latitude, longitude);

        return  toReturn;
    }

    private int getRangeFromFirebase() {
        initializeData();
        int result = currentUserData.getmAllowedRange();
        return result;
    }
}
