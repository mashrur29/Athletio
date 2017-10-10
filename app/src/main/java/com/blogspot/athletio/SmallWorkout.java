package com.blogspot.athletio;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tanvir on 10/10/17.
 */

public class SmallWorkout {
    int type;
    LatLng latLng;
    String uID;
    String uName;

    public SmallWorkout(int type, LatLng latLng, String uID, String uName) {
        this.type = type;
        this.latLng = latLng;
        this.uID = uID;
        this.uName = uName;
    }
}
