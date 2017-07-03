package com.roadtrippies.app.roadtrippies;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NearbyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private dbCon dbConnection = new dbCon();
    List<String> list_a = new ArrayList<>();
    List<String> list_n = new ArrayList<>();

    private SeekBar RadiusBar;
    Circle circle;
    int rad = 0;

    LocationManager mLocationManager;
    Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_slider);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocation = getLastKnownLocation();

        getInfoFromDB();
    }

    private void getInfoFromDB() {
        dbConnection.CONN();
        String a_q = "SELECT TOP 5 dbo.events.address FROM dbo.events";
        String n_q = "SELECT TOP 5 dbo.events.event FROM dbo.events";

        try {
            Statement stmt_a = dbConnection.conn.createStatement();
            Statement stmt_n = dbConnection.conn.createStatement();
            ResultSet rs_a = stmt_a.executeQuery(a_q);
            ResultSet rs_n = stmt_n.executeQuery(n_q);

            while(rs_a.next()){
                String addr = rs_a.getString(1);
                list_a.add(addr);
            }
            rs_a.close();

            while(rs_n.next()){
                String event = rs_n.getString(1);
                list_n.add(event);
            }
            rs_n.close();

        } catch (Exception e){
            Log.e("Debug", e.getMessage());
        }
    }

    @Override
    public void onMapReady(final GoogleMap gMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }

        gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        RadiusBar = (SeekBar)findViewById(R.id.seekBar);

        if(myLocation != null) {
            circle = gMap.addCircle(new CircleOptions()
                    .center(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                    .radius(0)
                    .strokeColor(Color.argb(255, 0, 255, 0))
                    .fillColor(Color.argb(50, 0, 255, 0)));
        }

        RadiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(circle != null) {
                    circle.setRadius(i * 75);
                    rad = i * 75;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "PING", Toast.LENGTH_LONG);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                GoogleMap map = gMap;
                int i = 0;
                for (String item : list_a) {
                    temp.setLatitude(getLocationFromAddress(getApplicationContext(), item).latitude);
                    temp.setLongitude(getLocationFromAddress(getApplicationContext(), item).longitude);

                    if(myLocation.distanceTo(temp) < circle.getRadius()) {
                        map.addMarker(new MarkerOptions().position(getLocationFromAddress(getApplicationContext(), item)).title(list_n.get(i)));
                        i++;
                    }
                }
            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
