package com.example.covidist;

public class DataManager {

    private int mID;
    private String mName;
    private String mEmail;
    private boolean mInRange;
    private Object mHomeLocation;
    private Object mLastLocation;
    private boolean mIsMainUser;
    private int mAllowedRange;

    public DataManager(){}

    public DataManager(int mID, String mName, String mEmail, boolean mInRange, Object mHomeLocation, Object mLastLocation, boolean mIsMainUser, int iRange) {
        this.mID = mID;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mInRange = mInRange;
        this.mHomeLocation = mHomeLocation;
        this.mLastLocation = mLastLocation;
        this.mIsMainUser = mIsMainUser;
        this.mAllowedRange = iRange;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public boolean ismInRange() {
        return mInRange;
    }

    public void setmInRange(boolean mInRange) {
        this.mInRange = mInRange;
    }

    public Object getmHomeLocation() {
        return mHomeLocation;
    }

    public void setmHomeLocation(Object mHomeLocation) {
        this.mHomeLocation = mHomeLocation;
    }

    public Object getmLastLocation() {
        return mLastLocation;
    }

    public void setmLastLocation(Object mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    public boolean ismIsMainUser() {
        return mIsMainUser;
    }

    public void setmIsMainUser(boolean mIsMainUser) {
        this.mIsMainUser = mIsMainUser;
    }

    public int getmAllowedRange() {
        return mAllowedRange;
    }

    public void setmAllowedRange(int mAllowedRange) {
        this.mAllowedRange = mAllowedRange;
    }
}
