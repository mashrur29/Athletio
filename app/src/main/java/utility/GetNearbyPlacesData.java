package utility;

/**
 * Created by zero639 on 10/1/17.
 */

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/*
    Reference: https://www.youtube.com/channel/UCGp1zoGrCqT54f32L6tGgqg
 */

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String GooglePlacesData;
    private GoogleMap NearbyPlaceMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        NearbyPlaceMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadURL downloadURL = new DownloadURL();
        try {
            GooglePlacesData = downloadURL.ReadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return GooglePlacesData;
    }


    // Here the list of nearby place is retrieved

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlaceList;
        NearbyPlacesApiDataParser parser = new NearbyPlacesApiDataParser();
        nearbyPlaceList = parser.parse(s);
        ShowNearbyPlaces(nearbyPlaceList);
    }

    // Fetches all the data from the list of Nearby Place

    private void ShowNearbyPlaces(List<HashMap<String, String>> NearbyPlaceList) {
        for (int i = 0; i < NearbyPlaceList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = NearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            NearbyPlaceMap.addMarker(markerOptions);
            NearbyPlaceMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            NearbyPlaceMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
