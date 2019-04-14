package com.slusher.android.bathroombreak;

import com.google.android.gms.maps.model.LatLng;

public class Bathroom {
    private String mReference;
    private String mName;
    private LatLng mLocation;
    private String mAddress;

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

    public Bathroom() {
        super();
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
