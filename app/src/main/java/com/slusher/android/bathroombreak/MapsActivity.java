package com.slusher.android.bathroombreak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
//        OnInfoWindowClickListener,
        OnMarkerClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private AddBathroomTask addBathroomTask;
    private LatLng currentLocation;
    private String API_KEY;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String LOG_TAG = "ListRest";
    private static final Long RADIUS = 4023L;
    private static final Float ZOOM_LEVEL = 13f;
    private static final Integer LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        API_KEY = getMetadata(this, "com.google.android.geo.API_KEY");
        addBathroomTask = new AddBathroomTask(MapsActivity.this);

        Toolbar appToolbar = findViewById(R.id.toolbar_header);

        setSupportActionBar(appToolbar);

        // allow network on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Initialize places
        Places.initialize(getApplicationContext(), API_KEY);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // get places
        //getPlaces();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

//            case R.id.action_favorite:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public static String getMetadata(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                return appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // if we can’t find it in the manifest, just return null
        }

        return null;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setTrafficEnabled(true);

//        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);

        addCurrentLocationMarkerWithPermissions();
    }

    private ArrayList<Bathroom> search(LatLng location, String name) {
        ArrayList<Bathroom> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("key=" + API_KEY);
            sb.append("&location=" + location.latitude + "," + location.longitude);
            sb.append("&radius=" + RADIUS);
            sb.append("&name=" + name);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            Bathroom[] bathroomList = new Bathroom[predsJsonArray.length()];

            for (int i = 0; i < predsJsonArray.length(); i++) {
                Bathroom place = new Bathroom();
                place.setId(predsJsonArray.getJSONObject(i).getString("place_id"));
                place.setReference(predsJsonArray.getJSONObject(i).getString("reference"));
                place.setName(predsJsonArray.getJSONObject(i).getString("name"));
                place.setAddress(predsJsonArray.getJSONObject(i).getString("vicinity"));
                JSONObject jsonLocation = predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                place.setLocation(new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng")));
                resultList.add(place);
                bathroomList[i] = place;
            }

            addBathroomTask.doInBackground(bathroomList);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }

    private void addBathroomMarkers() {
        ArrayList<String> searchTerms = new ArrayList<String>(Arrays.asList("starbucks", "mcdonalds", "cvs", "walgreens", "ampm"));

        ArrayList<Bathroom> list = new ArrayList<>();
        ArrayList<Bathroom> allBathrooms = search(currentLocation, join("|", searchTerms));
        list.addAll(allBathrooms);

        // Search each location and aggregate the results
//        for (String searchTerm : searchTerms)
//            list.addAll(search(currentLocation, searchTerm));

        for (Bathroom place : list) {
            System.out.println("Adding: " + place.getAddress() + " at " + place.getLocation().latitude + " " + place.getLocation().longitude);
            addMarker(place, false);
        }
    }

    private String join(String delimiter, List<String> input) {
        StringBuffer result = new StringBuffer("");

        for (String str : input) {
            if (result.length() == 0)
                result.append(str);
            else
                result.append(delimiter + str);
        }

        return result.toString();
    }

    private void addCurrentLocationMarkerWithPermissions() {
        // TODO: Cleanup the whole permissions request pattern according to: https://github.com/googlesamples/android-RuntimePermissionsBasic/blob/master/Application/src/main/java/com/example/android/basicpermissions/MainActivity.java
        if ((ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, INTERNET}, LOCATION_REQUEST_CODE);

            addCurrentLocationMarker();
        } else {
            addCurrentLocationMarker();
        }
    }

    @SuppressLint("MissingPermission")
    private void addCurrentLocationMarker() {
        mMap.setMyLocationEnabled(true);

        // get current location and search within this radius
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // Marlyn & Walter Location
//                      currentLocation = new LatLng(32.8012705, -116.9475112);

                    // Mitch's House
//                    currentLocation = new LatLng(33.879317, -117.213620);

                    // San Diego
                    //currentLocation = new LatLng(32.729888, -117.169623);

                    // Jonas
//                    currentLocation = new LatLng(33.7502155, -117.0544809);

                    // Show approximate location
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(currentLocation);
                    circleOptions.radius(RADIUS * 1.1);
                    circleOptions.fillColor(Color.argb(30, 0, 20, 150));
                    circleOptions.strokeColor(Color.argb(10, 0, 20, 150));

                    mMap.addCircle(circleOptions);


//                    addMarker(currentLocation, "Current Location", true);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL));
                    mMap.setInfoWindowAdapter(new BathroomInfoWindowAdapter(MapsActivity.this));
                    addBathroomMarkers();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    addCurrentLocationMarker();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Location permission missing", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private void addMarker(Bathroom place, boolean useDefaultMarker) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(place.getLocation())
                //.title(place.getName())
                //.snippet(place.getAddress())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_bathroom_location))
                .visible(true);

        if (useDefaultMarker)
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker());

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(place);
    }

//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        Toast toast = Toast.makeText(MapsActivity.this, "marker " + marker.getId() + " clicked!", Toast.LENGTH_LONG);
//        toast.show();
//    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast toast = Toast.makeText(MapsActivity.this, "marker " + marker.getId() + " clicked!", Toast.LENGTH_LONG);
        toast.show();
        return false;
    }
}
