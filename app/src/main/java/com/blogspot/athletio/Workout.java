package com.blogspot.athletio;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by tanvir on 8/27/17.
 */

public class Workout {
    public static final int RUNTYPE=0;
    public static final int CYCLINGTYPE=1;
    public static final int VRTYPE=2;


    int TYPE;
    double distanceInMeters;
    long timeInSec;
    int weight;
    String key;
    HashMap<String ,LatLng> map;
    Day day;
    int hour;
    int min;



    public Workout(int TYPE, double distanceInMeters, long timeInSec, int weight, Vector<LatLng> map,Day day,int hour,int min) {
        this.TYPE = TYPE;
        this.distanceInMeters = distanceInMeters;
        this.timeInSec = timeInSec;
        this.weight = weight;
        this.day=day;
        this.hour=hour;
        this.min=min;
        this.map=new HashMap<String, LatLng>();
        for (int i=0;i<map.size();i++){
            this.map.put("index"+Integer.toString(i),map.get(i));
        }
    }

    public Workout(int TYPE, double distanceInMeters, long timeInSec,Day day, int weight,int hour,int min) {
        this.TYPE = TYPE;
        this.distanceInMeters = distanceInMeters;
        this.timeInSec = timeInSec;
        this.weight = weight;
        this.day=day;
        this.hour=hour;
        this.min=min;
    }
    public Workout(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.distanceInMeters=jsonObjectParser.getDouble("distanceInMeters");
        this.TYPE=jsonObjectParser.getInt("TYPE");
        this.timeInSec= (long) jsonObjectParser.getLong("timeInSec");
        this.weight=jsonObjectParser.getInt("weight");
        this.day=new Day(jsonObjectParser.getString("day"));
        this.hour=jsonObjectParser.getInt("hour");
        this.min=jsonObjectParser.getInt("min");
        if(this.TYPE!=Workout.VRTYPE){
            this.map=new HashMap<String, LatLng>();
            HashMap<String,String> hm=new JsonObjectParser(jsonObjectParser.getString("map")).getMap();
            for(Map.Entry m:hm.entrySet()){
                double lat=new JsonObjectParser(m.getValue().toString()).getDouble("latitude");
                double lng=new JsonObjectParser(m.getValue().toString()).getDouble("longitude");
                this.map.put(m.getKey().toString(),new LatLng(lat,lng));
            }
        }
    }

    public int getCallorie(){
        //return callorie burned
        return Integer.parseInt("0");
    }

    LatLng getAt(int i){
        String str="index"+i;
        return (LatLng)map.get(str);
    }

    Vector<LatLng> getVector(){
        Vector<LatLng>ret=new Vector<LatLng>();
        for(int i=0;i<map.size();i++){
            ret.add(getAt(i));
        }
        return ret;

    }

    Vector<MapPoint> getmpVector(){
        Vector<MapPoint>ret=new Vector<MapPoint>();
        for(int i=0;i<map.size();i++){
            LatLng latLng=getAt(i);
            MapPoint mapPoint=new MapPoint(latLng.latitude,latLng.longitude);
            ret.add(mapPoint);
        }
        return ret;
    }


    @Override
    public String toString() {
        return "Workout{" +
                "TYPE=" + TYPE +
                ", distanceInMeters=" + distanceInMeters +
                ", timeInSec=" + timeInSec +
                ", weight=" + weight +
                ", key='" + key + '\'' +
                ", map=" + map +
                ", day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                '}';
    }
}
