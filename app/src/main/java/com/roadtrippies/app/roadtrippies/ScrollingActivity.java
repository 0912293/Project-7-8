package com.roadtrippies.app.roadtrippies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List list = new ArrayList();
        for (int i = 0; i < 100; i++){
            list.add(i,i);
        }

        ScrollView scroll = new ScrollView(this);               //creates scroll view
        LinearLayout linearLayout = new LinearLayout(this);     //creates linearlayout
        linearLayout.setOrientation(LinearLayout.VERTICAL);     //sets linearlayout to vertical
        final Context context = this;

        for( int i = 0; i < list.size(); i++ )                  //for each item in the list:
        {
            final TextView textView = new TextView(this);               //create new text view
            textView.setText(list.get(i).toString());                   //set the text to the item in the list
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {                            //creates on click for each event
                    LinearLayout eventlayout = new LinearLayout(context);
                    TextView eventTextView = new TextView(context);
                    eventTextView.setText("event information");
                    eventlayout.addView(eventTextView);
                    setContentView(eventlayout);
                }});
            textView.setPadding(60,150,0,0);
            textView.setTextSize(20);
            linearLayout.addView(textView);                     //adds the textview to the linearlayout
        }
        scroll.addView(linearLayout);                           //adds the linearlayout with textviews to the scroll view to make it scrollable
        setContentView(scroll);                                 //sets the view to the scroll view with linearlayout inside
    }
}
