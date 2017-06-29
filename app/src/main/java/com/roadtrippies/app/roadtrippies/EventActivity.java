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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

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

        ArrayList<String> genre = getIntent().getStringArrayListExtra("list");
        //System.out.println(getEventInfo(genre).toString());
        try {
            printResultset(getEventInfo(genre));
        } catch (SQLException e) {

        }
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


    private ResultSet getEventInfo(ArrayList<String> genre) {

        db.CONN();


        String query = "SELECT * FROM dbo.events WHERE dbo.events.genre = ?";
        ResultSet rs = null;
        try {
            for (String s : genre) {
                PreparedStatement preparedStmt = db.conn.prepareStatement(query);
                preparedStmt.setString(1, s);
                rs = preparedStmt.executeQuery();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private void printResultset(ResultSet rs) throws SQLException {

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int column = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i < column; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    String columnvalue = rs.getString(i);
                    System.out.println(columnvalue + " " + rsmd.getColumnName(i));

                }
            }
        } catch (SQLException e) {
            Log.d("Resultquery", "failed query");
        }
        System.out.println("");
    }
}
