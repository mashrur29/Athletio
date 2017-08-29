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
    HashMap<String ,LatLng> map;



    public Workout(int TYPE, double distanceInMeters, long timeInSec, int weight, Vector<LatLng> map) {
        this.TYPE = TYPE;
        this.distanceInMeters = distanceInMeters;
        this.timeInSec = timeInSec;
        this.weight = weight;
        this.map=new HashMap<String, LatLng>();
        for (int i=0;i<map.size();i++){
            this.map.put("index"+Integer.toString(i),map.get(i));
        }
    }

    public Workout(int TYPE, double distanceInMeters, long timeInSec, int weight) {
        this.TYPE = TYPE;
        this.distanceInMeters = distanceInMeters;
        this.timeInSec = timeInSec;
        this.weight = weight;
    }
    public Workout(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.distanceInMeters=jsonObjectParser.getDouble("distanceInMeters");
        this.TYPE=jsonObjectParser.getInt("TYPE");
        this.timeInSec= (long) jsonObjectParser.getLong("timeInSec");
        this.weight=jsonObjectParser.getInt("weight");

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

    Vector<LatLng> getVector(){
        Vector<LatLng>ret=new Vector<LatLng>();
        Map<String, LatLng> treemap = new TreeMap<>(this.map);
        for(Map.Entry m:treemap.entrySet()){
            // String ind=m.getKey().toString();
            //ind=ind.replace("index","");
            //int index=Integer.parseInt(ind);
            LatLng ll=(LatLng) m.getValue();
            ret.add(ll);
        }
        return ret;
    }

    @Override
    public String toString() {
        return "{" +
                "TYPE=" + TYPE +
                ", distanceInMeters=" + distanceInMeters +
                ", timeInSec=" + timeInSec +
                ", weight=" + weight +
                ", map=" + map +
                '}';
    }
}
