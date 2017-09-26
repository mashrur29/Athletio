package com.blogspot.athletio;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tanvir on 9/26/17.
 */

public class Event {

    public static final int RUNTYPE=0;
    public static final int CYCLINGTYPE=1;
    public static final int FOOTBALLTYPE=2;
    public static final int CRICKETTYPE=3;
    public static final int WALKTYPE=4;
    public static final String TAG="EVENT";

    String key;
    Day day;
    int hour;
    int min;
    String creatorID;
    LatLng start;
    LatLng stop;
    int type;
    double distanceInMeters;
    long durationInSec;

    public Event(Day day,int hour,int min, String creatorID, LatLng start, int type, long durationInSec) {
        this.day = day;
        this.hour=hour;
        this.min=min;
        this.creatorID = creatorID;
        this.start = start;
        this.type = type;
        this.durationInSec = durationInSec;
    }

    public Event(Day day,int hour,int min, String creatorID, LatLng start, LatLng stop, int type, double distanceInMeters, long durationInSec) {
        this.day = day;
        this.hour=hour;
        this.min=min;
        this.creatorID = creatorID;
        this.start = start;
        this.stop = stop;
        this.type = type;
        this.distanceInMeters = distanceInMeters;
        this.durationInSec = durationInSec;
    }

    public Event(String jsonStr) {

        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.type=jsonObjectParser.getInt("type");
        this.hour=jsonObjectParser.getInt("hour");
        this.min=jsonObjectParser.getInt("min");
        if(this.type==Event.RUNTYPE||this.type==Event.CYCLINGTYPE||this.type==Event.WALKTYPE){
            this.day=new Day(jsonObjectParser.getString("day"));
            this.creatorID=jsonObjectParser.getString("creatorID");
            this.start=new LatLng(new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("latitude"),new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("longitude"));
            this.stop=new LatLng(new JsonObjectParser(jsonObjectParser.getString("stop")).getDouble("latitude"),new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("longitude"));
            this.distanceInMeters=jsonObjectParser.getInt("distanceInMeters");
            this.durationInSec=jsonObjectParser.getLong("durationInSec");
        }
        else{
            this.day=new Day(jsonObjectParser.getString("day"));
            this.creatorID=jsonObjectParser.getString("creatorID");
            this.start=new LatLng(new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("latitude"),new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("longitude"));
            this.durationInSec=jsonObjectParser.getLong("durationInSec");
        }

    }

    @Override
    public String toString() {
        return "Event{" +
                "day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                ", creatorID='" + creatorID + '\'' +
                ", start=" + start +
                ", stop=" + stop +
                ", type=" + type +
                ", distanceInMeters=" + distanceInMeters +
                ", durationInSec=" + durationInSec +
                '}';
    }
}
