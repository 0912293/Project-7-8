package com.roadtrippies.app.roadtrippies;

import java.io.Serializable;

/**
 * Created by jesse on 29-6-2017.
 */

public class Event implements Serializable{

    private String name;
    private String location;
    private String genre;
    private String club;
    private String time;

    public Event(){}

    public void  setName(String name){

        this.name = name;
    }
    public void setLocation(String location){

        this.location = location;
    }
    public void setGenre(String genre){

        this.genre = genre;
    }

    public void setClub(String club){
        this.club = club;
    }

    public void setTime(String time){
        this.time = time;
    }
    public String getName(){
        return this.name;
    }

    public String getLocation(){
        return this.location;
    }

    public String getGenre(){
        return this.genre;
    }

    public String getClub(){
        return this.club;
    }

    public String getTime(){
        return this.time;
    }


}
