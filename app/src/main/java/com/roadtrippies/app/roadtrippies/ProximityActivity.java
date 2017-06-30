package com.roadtrippies.app.roadtrippies;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 26-Jun-17.
 */

public class ProximityActivity extends Activity {

    private final String PROX_ALERT_INTENT = "PROX_ALERT_INTENT";

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected NotificationManager notificationManager;
    protected NotificationCompat.Builder mBuilder;
    protected Intent resultIntent;
    private List<String> addresses;

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("MyLocation", "Lng: " + location.getLongitude() + " Lat: " + location.getLatitude());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proximity_activity);
        moveTaskToBack(true);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        addresses = getAddressesFromEvents();
        for (String str : addresses) {
            addressToNotification(str);
        }

        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException se){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        locationListener = new MyLocationListener();

    }
    public void addProximityAlert(double lat, double lng){
        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proxIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new MyReceiver(), filter);

        try {
            locationManager.addProximityAlert(lat, lng, 100f, -1, proxIntent);
            Log.d("Prox", "added Proximity alert");
        } catch(SecurityException se) {
            Intent intentSE = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intentSE);
        }
    }

    public void checkProximity(float longitude, float latitude){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        try {
            Log.d("Prox", "Trying");
            addProximityAlert(latitude, longitude);
        } catch (SecurityException se) {
            Log.d("Prox", "security exception");
        }
    }
    protected void createNotification(){
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_explore)
                .setContentTitle("Event Nearby!")
                .setContentText("You're near an event!")
                .setAutoCancel(true);
        resultIntent = new Intent(this, AssistantActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
    }

    public Address getLocationFromAddress(String strAddress){
        Geocoder geocoder = new Geocoder(this);
        List<Address> address;
        Location loc;

        try {
            address = geocoder.getFromLocationName(strAddress, 5);
            if (address.get(0) == null){
                Log.d("LocFromAddress", "address returns null");
                return null;
            }
            Address location = address.get(0);

            location.getLatitude();
            location.getLongitude();

            return location;

        } catch (Exception e) {
            Log.d("Address to Lat/Lng", e.getMessage());
            return null;
        }
    }

    public void addressToNotification(String addressStr){
        createNotification();
        if (getLocationFromAddress(addressStr) == null){
            int a = 1+1;
        } else {
        checkProximity((float) getLocationFromAddress(addressStr).getLongitude(),
                (float) getLocationFromAddress(addressStr).getLatitude());
        }
    }

    public List<String> getAddressesFromEvents(){
        List<String> eventList = new ArrayList<String>();
        ResultSet rs;
        int x = 1;
        dbCon db = new dbCon();
        db.CONN();
        String query = "SELECT dbo.events.address FROM dbo.events";

        try{
            PreparedStatement preparedStmt = db.conn.prepareStatement(query);
            Log.d("Debug",preparedStmt.toString());
            rs = preparedStmt.executeQuery();
        } catch (Exception e){
            rs = null;
            Log.d("Debug", e.getMessage());
        }
        try {
            if (rs != null) {
                while (rs.next()) {
                    eventList.add(rs.getString(x));
                }
            }
        } catch (Exception e) {
            Log.d("Debug", e.getMessage());
        }
        return eventList;
    }

    public class MyReceiver extends BroadcastReceiver {

        private final int NOTIF_ID = 666;

        @Override
        public void onReceive(Context context, Intent intent) {
            final String key = LocationManager.KEY_PROXIMITY_ENTERING;
            final Boolean entering = intent.getBooleanExtra(key, false);

            if (entering) {
                Toast.makeText(context, "entering", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "exiting", Toast.LENGTH_SHORT).show();
                notificationManager.cancel(NOTIF_ID);
                return;
            }

            notificationManager.notify(NOTIF_ID, mBuilder.build());
        }

    }

}
