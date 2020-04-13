package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Personal extends AppCompatActivity implements View.OnClickListener {
    private Button currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        currentLocation = findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.currentLocation:
                currentLocationClick();

        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void currentLocationClick()
    {
        Intent intent = new Intent(Personal.this, Map.class);
        startActivity(intent);
    }
}
