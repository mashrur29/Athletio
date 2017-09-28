package com.blogspot.athletio;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by tanvir on 9/26/17.
 */

public class Event {

    public static final int RUNTYPE=0;
    public static final int CYCLINGTYPE=1;
    public static final int FOOTBALLTYPE=2;
    public static final int CRICKETTYPE=3;
    public static final int WALKTYPE=4;
    public static final int OTHERTYPE=5;
    public static final String TAG="EVENT";
    public static final int ACTIVE=0;
    public static final int RUNNING=1;
    public static final int CANCELLED=2;
    public static final int FINISHED=3;

    String key;
    Day day;
    int hour;
    int min;
    String title;
    String description;
    String creatorID;
    String creatorName;
    LatLng start;
    LatLng stop;
    int type;
    double distanceInMeters;
    long durationInSec;
    int status;

    public Event(Day day,int hour,int min, String creatorID,String creatorName, LatLng start, int type, long durationInSec,String title,String description) {
        this.day = day;
        this.hour=hour;
        this.min=min;
        this.creatorID = creatorID;
        this.creatorName=creatorName;
        this.start = start;
        this.type = type;
        this.durationInSec = durationInSec;
        this.title=title;
        this.description=description;
        this.status=Event.ACTIVE;
    }

    public Event(Day day,int hour,int min, String creatorID,String creatorName, LatLng start, LatLng stop, int type, double distanceInMeters, long durationInSec,String title,String description) {
        this.day = day;
        this.hour=hour;
        this.min=min;
        this.creatorID = creatorID;
        this.creatorName=creatorName;
        this.start = start;
        this.stop = stop;
        this.type = type;
        this.distanceInMeters = distanceInMeters;
        this.durationInSec = durationInSec;
        this.title=title;
        this.description=description;
        this.status=Event.ACTIVE;
    }

    public Event(String jsonStr) {

        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.type=jsonObjectParser.getInt("type");
        this.hour=jsonObjectParser.getInt("hour");
        this.min=jsonObjectParser.getInt("min");
        this.title=jsonObjectParser.getString("title");
        this.description=jsonObjectParser.getString("description");
        this.creatorName=jsonObjectParser.getString("creatorName");
        this.day=new Day(jsonObjectParser.getString("day"));
        this.status=jsonObjectParser.getInt("status");
        this.creatorID=jsonObjectParser.getString("creatorID");
        this.durationInSec=jsonObjectParser.getLong("durationInSec");
        this.start=new LatLng(new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("latitude"),new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("longitude"));

        if(this.status!=Event.CANCELLED)
            this.status=getStatus();

        if(this.type==Event.RUNTYPE||this.type==Event.CYCLINGTYPE||this.type==Event.WALKTYPE){
            this.stop=new LatLng(new JsonObjectParser(jsonObjectParser.getString("stop")).getDouble("latitude"),new JsonObjectParser(jsonObjectParser.getString("start")).getDouble("longitude"));
            this.distanceInMeters=jsonObjectParser.getInt("distanceInMeters");
        }

    }

    int getStatus(){
        if(this.status==Event.CANCELLED)
            return Event.CANCELLED;

        Calendar cal = Calendar.getInstance();
        int mincount=this.min-cal.get(Calendar.MINUTE);
        int hourcount=this.hour-cal.get(Calendar.HOUR_OF_DAY);
        int daycount=this.day.day-cal.get(Calendar.DAY_OF_MONTH);
        int monthcount=(this.day.month-1)-cal.get(Calendar.MONTH);
        int yearcount=this.day.year-cal.get(Calendar.YEAR);

        cal.add(Calendar.MINUTE,mincount);
        cal.add(Calendar.HOUR_OF_DAY,hourcount);
        cal.add(Calendar.DAY_OF_MONTH,daycount);
        cal.add(Calendar.MONTH,monthcount);
        cal.add(Calendar.YEAR,yearcount);

        if(cal.compareTo(Calendar.getInstance())>0)
        {
            return Event.ACTIVE;
        }
        cal.add(Calendar.SECOND,(int)this.durationInSec);

        if(cal.compareTo(Calendar.getInstance())>0)
        {
            return Event.RUNNING;
        }
        return Event.FINISHED;
    }

    @Override
    public String toString() {
        return "Event{" +
                "key='" + key + '\'' +
                ", day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creatorID='" + creatorID + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", start=" + start +
                ", stop=" + stop +
                ", type=" + type +
                ", distanceInMeters=" + distanceInMeters +
                ", durationInSec=" + durationInSec +
                ", status=" + status +
                '}';
    }
}
