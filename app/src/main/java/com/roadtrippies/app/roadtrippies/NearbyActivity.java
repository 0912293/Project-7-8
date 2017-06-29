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
    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_slider);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    public void onMapReady(GoogleMap gMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
        }

        gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        int i = 0;
        int rad = 0;

        Location temp = new Location(LocationManager.GPS_PROVIDER);

        for (String item : list_a) {
            temp.setLatitude(getLocationFromAddress(this,item).latitude);
            temp.setLongitude(getLocationFromAddress(this, item).longitude);

            if(location.distanceTo(temp) < rad)
            gMap.addMarker(new MarkerOptions().position(getLocationFromAddress(this, item)).title(list_n.get(i)));
            i++;
        }

        RadiusBar = (SeekBar)findViewById(R.id.seekBar);

        final Circle circle = gMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(0)
                .strokeColor(Color.argb(255, 0, 255, 0))
                .fillColor(Color.argb(50, 0, 255, 0)));

        RadiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                circle.setRadius(i * 75);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "PING", Toast.LENGTH_LONG);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(), "PONG", Toast.LENGTH_LONG);
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

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
