package com.roadtrippies.app.roadtrippies;

import android.*;
import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},3);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != -1) {
            new AsyncDownload(getBaseContext()).execute("");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.headerTitleTV);
        TextView nar_email = (TextView)hView.findViewById(R.id.headerEmailTV);
        ImageView nav_image = (ImageView) hView.findViewById(R.id.nav_image);
        if (GlobalClass.getInstance().name!=null && !GlobalClass.getInstance().name.isEmpty()) {
            nav_user.setText(GlobalClass.getInstance().name);
            nar_email.setText(GlobalClass.getInstance().email);
            if(GlobalClass.getInstance().profileImgUrl!=null) {
                nav_image.setVisibility(View.VISIBLE);
                Glide.with(this).load(GlobalClass.getInstance().profileImgUrl).into(nav_image);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_virtual) {
            startActivity(new Intent(getBaseContext(), AssistantActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));

        } else if (id == R.id.nav_send) {
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"Roadtrippies@support.info"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Regarding: Feedback Roadtrippies app");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Insert feedback here...");

            /* Send it off to the Activity-Chooser */
            startActivity(emailIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class AsyncDownload extends AsyncTask<String, Void, Integer> {
        private final String PROX_ALERT_INTENT = "PROX_ALERT_INTENT";
        private Context context;
        private LocationManager locationManager;
        private LocationListener locationListener;
        private NotificationManager notificationManager;
        private NotificationCompat.Builder mBuilder;
        private Intent resultIntent;
        private List<String> addresses;

        private AsyncDownload(Context context){
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params){
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            addressToNotification("Rotterdam Wijnhaven 99");
            addressToNotification("Rotterdam Glashaven 6");

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
            return 1;
        }

        private void addressToNotification(String addressStr){
            createNotification(addressStr);
            if (getLocationFromAddress(addressStr) != null){
                try {
                    checkProximity((float) getLocationFromAddress(addressStr).getLongitude(),
                            (float) getLocationFromAddress(addressStr).getLatitude());
                } catch (NullPointerException e) {
                    Log.d("Debug", e.getMessage());
                }
            }
        }

        private void createNotification(String addressStr){
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_explore)
                    .setContentTitle("Event Nearby!")
                    .setContentText("You're near an event")
                    .setAutoCancel(true);
            resultIntent = new Intent(context, AssistantActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
        }

        private void checkProximity(float longitude, float latitude){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            try {
                Log.d("Prox", "Trying");
                addAlert(latitude, longitude);
            } catch (SecurityException se) {
                Log.d("Prox", "security exception");
            }
        }

        private void addAlert(double lat, double lng){
            Intent intent = new Intent(PROX_ALERT_INTENT);
            PendingIntent proxIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

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

        private Address getLocationFromAddress(String strAddress){
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            Address address;

            try {
                addresses = geocoder.getFromLocationName(strAddress, 5);
                if (addresses.get(0) == null){
                    Log.d("LocFromAddress", "address returns null");
                    return null;
                }
                address = addresses.get(0);

                return address;

            } catch (Exception e) {
                Log.d("Address to Lat/Lng", e.getMessage());
                return null;
            }
        }

        private List<String> getAddressesFromEvents(){
            List<String> eventList = new ArrayList<>();
            ResultSet rs;
            int x = 1;
            DBCon db = new DBCon();
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
                    Log.d("Debug", "entering");
                    notificationManager.notify(NOTIF_ID, mBuilder.build());
                } else {
                    Log.d("Debug", "exiting");
                    notificationManager.cancel(NOTIF_ID);
                }

            }

        }

    }

}
