package com.roadtrippies.app.roadtrippies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        String genre = getIntent().getStringExtra("genre");
        System.out.println(getEventInfo(genre).toString());
//        Event event = new Event();
//        event.setName("Nachtcollege");
//        event.setLocation("Kruiskade");
//        event.setGenre("Fuckboymuziek");
//        event.setClub("Villa thalia");
//        event.setTime("5 august");
//
//        evt_title.setText(event.getName());
//        evt_loc.setText(event.getLocation());
//        evt_club.setText(event.getClub());
//        evt_dt.setText(event.getTime());
//        evt_genre.setText(event.getGenre());
    }


    private ResultSet getEventInfo(String genre) {

        db.CONN();
        String query = "SELECT * FROM dbo.events WHERE dbo.events.genre = ?";
        ResultSet rs = null;
        try {
            PreparedStatement preparedStmt = db.conn.prepareStatement(query);
            preparedStmt.setString(1, genre);
            rs = preparedStmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
}
