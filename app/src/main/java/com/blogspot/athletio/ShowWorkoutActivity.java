package com.blogspot.athletio;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ShowWorkoutActivity extends AppCompatActivity implements OnMapReadyCallback {
    Workout workout;
    DatabaseReference mDatabase;
    GoogleMap mgoogleMap;



    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_workout);
        setupUI();
        if(gservicesavailable())
        initMap();
        String key=getIntent().getStringExtra("WorkoutKey");
        mDatabase= FirebaseDatabase.getInstance().getReference().child("workouts").child(key);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    workout=new Workout(dataSnapshot.getValue().toString());
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setupUI() {
        tv=(TextView)findViewById(R.id.showworkouttv);
    }
    private void updateUI() {
        Vector<LatLng>latLngs=workout.getVector();
        MarkerOptions markerStart=new MarkerOptions().title("Start").position(latLngs.get(0));
        mgoogleMap.addMarker(markerStart);
        MarkerOptions markerEnd=new MarkerOptions().title("End").position(latLngs.get(latLngs.size()-1));
        mgoogleMap.addMarker(markerEnd);
        for (int i=0;i<latLngs.size()-1;i++){
            PolylineOptions line=new PolylineOptions().add(latLngs.get(i),latLngs.get(i+1)).width(10).color(Color.BLUE);
            mgoogleMap.addPolyline(line);
        }
        gotoloc(latLngs.get(0).latitude,latLngs.get(0).longitude,15);
        getAddress(latLngs.get(0).latitude,latLngs.get(0).longitude);
        tv.setText(workout.distanceInMeters+" "+workout.timeInSec);

    }



    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showworkoutmap);
        mapFragment.getMapAsync(this);

    }

    public boolean gservicesavailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isavailable = api.isGooglePlayServicesAvailable(this);
        if (isavailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isavailable)) {
            Dialog dialog = api.getErrorDialog(this, isavailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Could not connect to Play Services", Toast.LENGTH_LONG).show();
        }
        return false;

    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ShowWorkoutActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        } catch (IOException e) {

        }
        return null;
    }

    private void gotoloc(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mgoogleMap.moveCamera(update);

    }

    private void gotoloc(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mgoogleMap.moveCamera(update);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap=googleMap;
    }
}
