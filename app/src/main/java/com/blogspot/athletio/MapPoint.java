package com.blogspot.athletio;

import java.util.Vector;

/**
 * Created by tanvir on 8/25/17.
 */

public class MapPoint {
    double lat;
    double lng;

    public MapPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public MapPoint(String str){
        JsonObjectParser jsonObjectParser=new JsonObjectParser(str);
        this.lat=jsonObjectParser.getDouble("lat");
        this.lng=jsonObjectParser.getDouble("lng");
    }

    @Override
    public String toString() {
        return "{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
