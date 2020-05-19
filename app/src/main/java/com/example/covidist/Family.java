package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Family extends AppCompatActivity implements View.OnClickListener {

    private Button addFamilyMember;
    private Button deleteFamilyMember;
    private Button findFamilyMember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        addFamilyMember = findViewById(R.id.AddFamilyMember);
        addFamilyMember.setOnClickListener(this);

        deleteFamilyMember = findViewById(R.id.DeleteFamilyMember);
        deleteFamilyMember.setOnClickListener(this);

        findFamilyMember = findViewById(R.id.FindFamilyMember);
        findFamilyMember.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddFamilyMember:
                addFamily();
                break;
            case R.id.DeleteFamilyMember:
                deleteFamily();
                break;
            case R.id.FindFamilyMember:
                findFamily();
                break;

        }
    }

    private void findFamily()
    {
        Intent intent = new Intent(Family.this,FindMember.class);
        startActivity(intent);
    }

    private void deleteFamily()
    {
        Intent intent = new Intent(Family.this,DeleteMember.class);
        startActivity(intent);
    }

    private void addFamily()
    {
        Intent intent = new Intent(Family.this,AddMember.class);
        startActivity(intent);
    }

}
