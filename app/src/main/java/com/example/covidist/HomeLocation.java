package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

public class HomeLocation extends AppCompatActivity implements View.OnClickListener {

    private Button chooseBtn;
    private Button updateBtn;
    private FirebaseAssistant mFirebaseAssistant;
    private LatLng mChosenLocation;
    private TextView addressText;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_location);

        mFirebaseAssistant = FirebaseAssistant.getInstance();
        chooseBtn = findViewById(R.id.chooseBtn);
        chooseBtn.setOnClickListener(this);
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);
        addressText = findViewById(R.id.addressText);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.chooseBtn:
                chooseClick();
                break;
            case R.id.updateBtn:
                updateClick();
                break;
        }
    }

    private void updateClick() {
        mFirebaseAssistant.updateUser("mHomeLocation", mChosenLocation, new FirebaseAssistant.DataStatus() {
            @Override
            public void DataIsLoaded(List<DataManager> iDataManagerList, List<String> iKeys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(HomeLocation.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    private void chooseClick() {
        Intent intent = new Intent(HomeLocation.this, ChooseLocationMap.class);
        intent.putExtra("Location", (Location) getIntent().getExtras().get("Location"));
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mChosenLocation = (LatLng) data.getExtras().get("ChosenLocation");
                Address address = getAddressFromLatLng(mChosenLocation);
                addressText.setText(address.getAddressLine(0));
                updateBtn.setEnabled(true);
            }
        }
    }

    private LatLng getLatLngFromAddress(String address){

        Geocoder geocoder=new Geocoder(HomeLocation.this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if(addressList!=null){
                Address singleAddress = addressList.get(0);
                LatLng latLng=new LatLng(singleAddress.getLatitude(),singleAddress.getLongitude());
                return latLng;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private Address getAddressFromLatLng(LatLng latLng){
        Geocoder geocoder=new Geocoder(HomeLocation.this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if(addresses!=null){
                Address address=addresses.get(0);
                return address;
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
