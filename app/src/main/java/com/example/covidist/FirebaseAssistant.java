package com.example.covidist;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseAssistant implements ValueEventListener {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceUsers;
    private List<DataManager> mUsers = new ArrayList<>();
    private static FirebaseAssistant mInstance = null;
    private String mCurrentUserKey;

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUsers.clear();
        for(DataSnapshot d : dataSnapshot.getChildren()){
            DataManager user = d.getValue(DataManager.class);
            mUsers.add(user);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }

    public interface DataStatus{
        void DataIsLoaded(List<DataManager> iDataManagerList, List<String> iKeys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public static FirebaseAssistant getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new FirebaseAssistant();
        }

        return mInstance;
    }

    public FirebaseAssistant() {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mReferenceUsers = mDatabase.getReference("Users");
        this.mReferenceUsers.addValueEventListener(this);
    }

    public void addUser(DataManager iUser, final DataStatus iDataStatus){
        mCurrentUserKey = mReferenceUsers.push().getKey();
        mReferenceUsers.child(mCurrentUserKey).setValue(iUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                iDataStatus.DataIsInserted();
            }
        });
    }

    public void updateUser(String iFieldName, Object iValue, final DataStatus iDataStatus){
        switch(iFieldName){
            case "mAllowedRange":
                mReferenceUsers.child(mCurrentUserKey).child(iFieldName).setValue((int)iValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iDataStatus.DataIsUpdated();
                    }
                });
                break;
            case "mHomeLocation":
            case "mLastLocation":
                mReferenceUsers.child(mCurrentUserKey).child(iFieldName).setValue(iValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iDataStatus.DataIsUpdated();
                    }
                });
                break;
        }
    }

    public List<DataManager> getmUsers() {
        return mUsers;
    }

    public void deleteUser (String iKey, final DataStatus iDataStatus)
    {
        mReferenceUsers.child(iKey).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        iDataStatus .DataIsDeleted();
                    }
                });
    }
}
