package general;

import com.google.android.gms.maps.model.LatLng;

import utility.JsonObjectParser;

/**
 * Created by tanvir on 10/10/17.
 */


/// Holds small info of a workout
public class SmallWorkout {
    public int type;
    public LatLng latLng;
    public String uID;
    public String uName;

    public SmallWorkout(int type, LatLng latLng, String uID, String uName) {
        this.type = type;
        this.latLng = latLng;
        this.uID = uID;
        this.uName = uName;
    }

    public SmallWorkout(String jsonStr) {
        JsonObjectParser jsonObjectParser=new JsonObjectParser(jsonStr);
        this.type=jsonObjectParser.getInt("type");
        this.uName=jsonObjectParser.getString("uName");
        this.uID=jsonObjectParser.getString("uID");
        JsonObjectParser ll=new JsonObjectParser(jsonObjectParser.getString("latLng"));
        this.latLng=new LatLng(ll.getDouble("latitude"),ll.getDouble("longitude"));
    }

    @Override
    public String toString() {
        return "SmallWorkout{" +
                "type=" + type +
                ", latLng=" + latLng +
                ", uID='" + uID + '\'' +
                ", uName='" + uName + '\'' +
                '}';
    }
}
