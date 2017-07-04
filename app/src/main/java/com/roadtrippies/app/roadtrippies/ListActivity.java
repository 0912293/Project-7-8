package com.roadtrippies.app.roadtrippies;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    DBCon db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ScrollView scroll = new ScrollView(this);               //creates scroll view

        LinearLayout linearLayout = new LinearLayout(this);     //creates linearlayout
        linearLayout.setOrientation(LinearLayout.VERTICAL);     //sets linearlayout to vertical
        final Context context = this;
        Log.d("Debug","getting list of events");
        final ArrayList<Event> listofevents = getEventInfo();
        System.out.println(listofevents.size());
        Log.d("Debug","got list of events");
        for(final Event e : listofevents)                  //for each item in the list:
        {
            final TextView textView = new TextView(this);               //create new text view
            textView.setText(e.getName() + " - " + e.getGenre());    //set the text to the item in the list
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {                            //creates on click for each event
                    Intent intent = new Intent(getBaseContext(), EventActivity.class);
                    intent.putExtra("event", (Serializable)e);
                    startActivity(intent);
                }});
            textView.setPadding(60,150,0,0);
            textView.setTextSize(20);
            linearLayout.addView(textView);                     //adds the textview to the linearlayout
        }
        scroll.addView(linearLayout);                           //adds the linearlayout with textviews to the scroll view to make it scrollable
        setContentView(scroll);                                 //sets the view to the scroll view with linearlayout inside
    }

    private ArrayList<Event> getEventInfo(){
        db=new DBCon();
        db.CONN();

        ArrayList<Event> eventList = new ArrayList<>();
        //ArrayList<ResultSet> rsList = new ArrayList<>();

        String query = "SELECT * FROM dbo.events";
        ResultSet rs;
        Log.d("Debug","executed query");
        try {
//            for (String s : genre) {                    //runs query for every genre in genre list.
                PreparedStatement preparedStmt = db.conn.prepareStatement(query);
//                preparedStmt.setString(1, s);
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
//            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return eventList;                               //returns list of Event objects.
    }
}
