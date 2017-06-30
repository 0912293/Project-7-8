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
import java.util.stream.Collectors;

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

        ArrayList<String> genrelist = new ArrayList<String>(new LinkedHashSet<String>(genre));
        ArrayList<Event> listofevents = getEventInfo(genrelist);

        System.out.println(listofevents.size());
    }



    private ArrayList<Event> getEventInfo(ArrayList<String> genre){

        db.CONN();

        ArrayList<Event> eventList = new ArrayList<>();
        //ArrayList<ResultSet> rsList = new ArrayList<>();

        String query = "SELECT * FROM dbo.events WHERE dbo.events.genre = ?";
        ResultSet rs;

        try {
            for (String s : genre) {                    //runs query for every genre in genre list.
                PreparedStatement preparedStmt = db.conn.prepareStatement(query);
                preparedStmt.setString(1, s);
                rs = preparedStmt.executeQuery();


                while (rs.next()) {
                    Event event = new Event();          //adds all items from the tablerow into Event object.
                    event.setName(rs.getString(1));
                    event.setClub(rs.getString(2));
                    event.setLocation(rs.getString(3));
                    event.setTime(rs.getString(4));
                    event.setGenre(rs.getString(5));
                    eventList.add(event);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return eventList;                               //returns list of Event objects.
    }
}
