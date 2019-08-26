package com.slusher.android.bathroombreak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BathroomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private Bathroom bathroom;

    public BathroomInfoWindowAdapter(Context context) {
        this.mContext = context;
        mWindow = LayoutInflater
                .from(mContext)
                .inflate(R.layout.bathroom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        bathroom = (Bathroom)marker.getTag();

        String title = bathroom.getName();
        String address = bathroom.getAddress();
        double rating = bathroom.getRating();

        TextView tvTitle = (TextView)view.findViewById(R.id.title);
        TextView tvAddress = (TextView)view.findViewById(R.id.bathroom_address);
        MaterialRatingBar mrbRating = (MaterialRatingBar)view.findViewById(R.id.ratingBar);

        tvTitle.setText(title);
        tvAddress.setText(address);
        mrbRating.setRating((float)rating);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
