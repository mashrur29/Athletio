package utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zero639 on 10/1/17.
 */

/*
    Gives the Place list for a request of Nearby Place
 */

public class NearbyPlacesApiDataParser {

    // Returns details of a place

    private HashMap<String, String> getPlace(JSONObject GooglePlaceJson) {
        HashMap<String, String> GooglePlaceMap = new HashMap<>();
        String PlaceName = "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if (!GooglePlaceJson.isNull("name")) {
                PlaceName = GooglePlaceJson.getString("name");
            }
            if (!GooglePlaceJson.isNull("vicinity")) {
                vicinity = GooglePlaceJson.getString("vicinity");
            }

            latitude = GooglePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = GooglePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = GooglePlaceJson.getString("reference");
            GooglePlaceMap.put("place_name", PlaceName);
            GooglePlaceMap.put("vicinity", vicinity);
            GooglePlaceMap.put("lat", latitude);
            GooglePlaceMap.put("lng", longitude);
            GooglePlaceMap.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return GooglePlaceMap;

    }

    // Returns places after parsing the JSON

    private List<HashMap<String, String>> GetPlace(JSONArray JsonArray) {
        int count = JsonArray.length();
        List<HashMap<String, String>> PlaceList = new ArrayList<>();
        HashMap<String, String> PlaceMap = null;

        for (int i = 0; i < count; i++) {
            try {
                PlaceMap = getPlace((JSONObject) JsonArray.get(i));
                PlaceList.add(PlaceMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return PlaceList;
    }

    // Here the JSON is fed, from which we get the results of nearby place

    public List<HashMap<String, String>> parse(String JsonData) {
        JSONArray JsonArray = null;
        JSONObject JsonObject;

        try {
            JsonObject = new JSONObject(JsonData);
            JsonArray = JsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GetPlace(JsonArray);
    }
}
