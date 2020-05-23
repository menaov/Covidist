package com.example.covidist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;

public class Entry_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAssistant mFirebaseAssistant;
    boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_);

        isUpdate = false;
        mFirebaseAssistant = FirebaseAssistant.getInstance();
        mAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );
        showSignInOptions();
    }

    private void showSignInOptions() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setTheme(R.style.SignInTheme).build(), REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isUpdate)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Entry_Activity.this, Personal.class);
                    startActivity(intent);
                }
            }, 5000);
        }


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
                        isUpdate = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Entry_Activity.this, Personal.class);
                                startActivity(intent);
                            }
                        }, 5000);
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
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
