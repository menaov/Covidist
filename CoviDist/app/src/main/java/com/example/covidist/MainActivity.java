package com.example.covidist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button personalMenu;
    private Button familyMenu;
    private static final int REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private List<AuthUI.IdpConfig> providers;
    private Button signOutBtn;
    private FirebaseAssistant mFirebaseAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAssistant = FirebaseAssistant.getInstance();
        personalMenu = findViewById(R.id.personalMenu);
        familyMenu = findViewById(R.id.familyMenu);
        personalMenu.setOnClickListener(this);
        familyMenu.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        signOutBtn = findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(this);
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()//,
                //new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        showSignInOptions();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.personalMenu:
                personalMenu();
                break;
            case R.id.familyMenu:
                familyMenu();
                break;
            case R.id.signOutBtn:
                signOut();
                break;
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                signOutBtn.setEnabled(false);
                showSignInOptions();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSignInOptions() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setTheme(R.style.SignInTheme).build(), REQUEST_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DataManager toAdd;
        if(requestCode == REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                signOutBtn.setEnabled(true);
                Location location = new Location("");
                location.setLatitude(32.013753);
                location.setLongitude(34.773860);
                toAdd = new DataManager(0,
                        user.getDisplayName(),
                        user.getEmail(),
                        true,
                        location,location,true,100);
                mFirebaseAssistant.addUser(toAdd, new FirebaseAssistant.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<DataManager> iDataManagerList, List<String> iKeys) {
                    }

                    @Override
                    public void DataIsInserted() {
                    }

                    @Override
                    public void DataIsUpdated() {
                    }

                    @Override
                    public void DataIsDeleted() {
                    }
                });

            }
            else{
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
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
