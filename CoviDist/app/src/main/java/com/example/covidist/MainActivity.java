package com.example.covidist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button personalMenu;
    private Button familyMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personalMenu = findViewById(R.id.personalMenu);
        familyMenu = findViewById(R.id.familyMenu);

        personalMenu.setOnClickListener(this);
        familyMenu.setOnClickListener(this);


    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.personalMenu:
                personalMenu();
                Toast.makeText(this, "Personal menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.familyMenu:
                familyMenu();
                Toast.makeText(this, "Family menu", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
    public void personalMenu()
    {
        Intent intent = new Intent(MainActivity.this,Personal.class);
        startActivity(intent);
    }
    public void familyMenu()
    {
        Intent intent = new Intent(MainActivity.this,Family.class);
        startActivity(intent);
    }

}
