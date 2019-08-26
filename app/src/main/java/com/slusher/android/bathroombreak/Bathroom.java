package com.slusher.android.bathroombreak;

import com.google.android.gms.maps.model.LatLng;

public class Bathroom {
    private String mId;
    private String mReference;
    private String mName;
    private LatLng mLocation;
    private String mAddress;
    private double mRating;
    private boolean mHasBabyChangingStation;
    private boolean mHasDisability;
    private boolean mHasGenderNeutralBathrooms;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public void setLocation(LatLng location) {
        this.mLocation = location;
    }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getRating() {
        double randomRating = Math.random() * 4 + 1;
        return randomRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public boolean isHasBabyChangingStation() {
        return mHasBabyChangingStation;
    }

    public void setHasBabyChangingStation(boolean mHasBabyChangingStation) {
        this.mHasBabyChangingStation = mHasBabyChangingStation;
    }

    public boolean ismHasDisability() {
        return mHasDisability;
    }

    public void setHasDisability(boolean mHasDisability) {
        this.mHasDisability = mHasDisability;
    }

    public boolean isHasGenderNeutralBathrooms() {
        return mHasGenderNeutralBathrooms;
    }

    public void setHasGenderNeutralBathrooms(boolean mHasGenderNeutralBathrooms) {
        this.mHasGenderNeutralBathrooms = mHasGenderNeutralBathrooms;
    }

    public Bathroom() {
        super();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
