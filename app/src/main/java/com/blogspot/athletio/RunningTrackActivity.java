package com.blogspot.athletio;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import general.Day;
import general.SmallWorkout;
import general.Workout;
import storage.SharedPrefData;

public class RunningTrackActivity  extends AppCompatActivity implements OnMapReadyCallback {

    final static int INTERVAL=5000;

    GoogleMap mgoogleMap;
    Vector<LatLng> latLngs=new Vector<LatLng>();
    LocationManager locationManager;
    android.location.LocationListener locationListener;
    double dist=0;
    int tick=0;
    boolean first=true;
    long time=0;
    Date initTime;
    Thread t;
    boolean b=true;

    DatabaseReference mDatabase,mCurrentWorkoutDb;
    FirebaseAuth mAuth;

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_track);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("workouts");
        mCurrentWorkoutDb=FirebaseDatabase.getInstance().getReference().child("currentworkouts");
        String key=mCurrentWorkoutDb.push().getKey();
        mCurrentWorkoutDb=mCurrentWorkoutDb.child(key);

        setupUI();
        initTime=new Date();

        if (gservicesavailable()) {
            initMap();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            locationListener = new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
                    if(first){
                        gotoloc(ll.latitude,ll.longitude,15);
                        first=false;
                        t = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    while (!isInterrupted()&&b) {
                                        Thread.sleep(1000);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                time=new Date().getTime()-initTime.getTime();
                                                time/=1000;
                                                updateUI();
                                            }
                                        });
                                    }
                                } catch (InterruptedException e) {
                                }
                            }
                        };

                        t.start();

                    }
                    else {
                        CameraUpdate update= CameraUpdateFactory.newLatLng(ll);
                        mgoogleMap.animateCamera(update);
                    }
                    latLngs.add(ll);
                    mCurrentWorkoutDb.setValue(new SmallWorkout(0,ll,mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName()));
                    mgoogleMap.clear();
                    MarkerOptions markerStart=new MarkerOptions().title("Start").position(latLngs.get(0));
                    mgoogleMap.addMarker(markerStart);
                    MarkerOptions markerEnd=new MarkerOptions().title("End").position(latLngs.get(latLngs.size()-1));
                    mgoogleMap.addMarker(markerEnd);
                    for (int i=0;i<latLngs.size()-1;i++){
                        PolylineOptions line=new PolylineOptions().add(latLngs.get(i),latLngs.get(i+1)).width(10).color(Color.BLUE);
                        mgoogleMap.addPolyline(line);
                    }
                    if(latLngs.size()>1){
                        LatLng last=latLngs.get(latLngs.size()-2);
                        dist+=haversine(ll.latitude,ll.longitude,last.latitude,last.longitude);
                    }
                    tick++;
                    if(tick>200){

                        startActivity(new Intent(RunningTrackActivity.this,TrackWorkoutMenuActivity.class));
                        finish();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            };
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled){
                Toast.makeText(this,"Turn Location on",Toast.LENGTH_SHORT).show();
            }
            if(isNetworkEnabled)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,INTERVAL,0,locationListener);

            else if(isGPSEnabled)
                locationManager.requestLocationUpdates("gps", INTERVAL, 0, locationListener);

        }
    }

    private void updateUI() {
        tv.setText("Distance: "+Math.round(dist*100)/100+"m"+"\n"+"Time: "+time);
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.runningtracktv);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgoogleMap=googleMap;
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.runningmap);
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
    protected void onDestroy() {
        super.onDestroy();
        b=false;
        String key = mDatabase.push().getKey();
        SharedPreferences pref = RunningTrackActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
        Workout pushWorkout=new Workout(Workout.RUNTYPE,dist,  time,pref.getInt(SharedPrefData.WEIGHT, 0),latLngs,new Day(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
        mDatabase.child(key).setValue(pushWorkout);
        locationManager.removeUpdates(locationListener);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("workouts").child(key).setValue(key);
        mCurrentWorkoutDb.setValue(null);

    }

    public double haversine(
            double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c*1000;
        return d;
    }
}
