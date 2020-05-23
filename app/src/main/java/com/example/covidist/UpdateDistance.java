package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class UpdateDistance extends AppCompatActivity implements View.OnClickListener {

    private EditText rangeInput;
    private Button updateBtn;
    private FirebaseAssistant mFirebaseAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_distance);

        rangeInput = findViewById(R.id.rangeInput);
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);
        mFirebaseAssistant = FirebaseAssistant.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.updateBtn:
                updateRangeInFirebase(Integer.parseInt(rangeInput.getText().toString()));
                break;
        }
    }

    private void updateRangeInFirebase(int iValue) {
        mFirebaseAssistant.updateUser("mAllowedRange", iValue, new FirebaseAssistant.DataStatus() {
            @Override
            public void DataIsLoaded(List<DataManager> iDataManagerList, List<String> iKeys) {

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {
                Toast.makeText(UpdateDistance.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
}
