package general;

import utility.JsonObjectParser;

/**
 * Created by tanvir on 8/25/17.
 */

public class MapPoint {
    public double lat;
    public double lng;

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
