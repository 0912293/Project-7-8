package com.roadtrippies.app.roadtrippies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class EventActivity extends AppCompatActivity {

    DBCon db = new DBCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView evt_title = (TextView) findViewById(R.id.event_title);
        TextView evt_loc = (TextView) findViewById(R.id.event_loc);
        TextView evt_club = (TextView) findViewById(R.id.event_club);
        TextView evt_genre = (TextView) findViewById(R.id.event_genre);
        TextView evt_dt = (TextView) findViewById(R.id.event_dt);

        Event event = (Event) getIntent().getExtras().getSerializable("event");

        evt_title.setText(event.getName());
        evt_loc.setText(event.getLocation());
        evt_club.setText(event.getClub());
        evt_genre.setText(event.getGenre());
        evt_dt.setText(event.getTime());


        //ArrayList<String> genre = getIntent().getStringArrayListExtra("list");

        //ArrayList<String> genrelist = new ArrayList<String>(new LinkedHashSet<String>(genre));
        //ArrayList<Event> listofevents = getEventInfo(genrelist);


    }




}
