package com.blogspot.athletio;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import general.SmallWorkout;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class OnlineWorkoutActivity extends AppCompatActivity  implements OnMapReadyCallback {
    final static int INTERVAL=5000;

    GoogleMap mgoogleMap;
    DatabaseReference mDatabase;
    LocationManager locationManager;
    android.location.LocationListener locationListener;
    boolean first=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_workout);
        if(gservicesavailable()){
            initMap();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            locationListener = new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
                    if(first){
                        gotoloc(ll.latitude,ll.longitude,15);
                        first=false;
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

            // getting network statusTextview
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled){
                Toast.makeText(this,"Turn Location on",Toast.LENGTH_SHORT).show();
            }
            if(isNetworkEnabled)
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,INTERVAL,0,locationListener);

            else if(isGPSEnabled)
                locationManager.requestLocationUpdates("gps", INTERVAL, 0, locationListener);

            mDatabase= FirebaseDatabase.getInstance().getReference().child("currentworkouts");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        for(DataSnapshot d : dataSnapshot.getChildren()) {
                            SmallWorkout smallWorkout=new SmallWorkout(d.getValue().toString());
                            String title;
                            if(smallWorkout.type==0)
                                title="running";
                            else
                                title="cycling";
                            MarkerOptions marker=new MarkerOptions().title(smallWorkout.uName+" is "+title).position(smallWorkout.latLng);
                            mgoogleMap.addMarker(marker);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.online_workout_layout_map);
        mapFragment.getMapAsync(this);

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

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTrackWorkout:
                startActivity(new Intent(this, TrackWorkoutMenuActivity.class));
                return true;
            case R.id.menuOnlineWorkout:
                startActivity(new Intent(this, OnlineWorkoutActivity.class));
                return true;
            case R.id.menuMyWorkouts:
                startActivity(new Intent(this, MyWorkoutsActivity.class));
                return true;
            case R.id.menuExcersices:
                startActivity(new Intent(this, ExercisesActivity.class));
                return true;
            case R.id.menuSocial:
                startActivity(new Intent(this, NewsFeedActivity.class));
                return true;
            case R.id.menuEvents:
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            case R.id.menuEventReminder:
                startActivity(new Intent(this, ShowEventRemindersActivity.class));
                return true;
            case R.id.menuCreateEvent:
                startActivity(new Intent(this, CreateEventActivity.class));
                return true;
            case R.id.menuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menuSignOut:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void signOut(){
        SharedPrefData sharedPrefData=new SharedPrefData(this);
        sharedPrefData.clear();
        Intent intent=new Intent(this, FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(this, StepDetector.class);
        stopService(intent2);
        FirebaseAuth.getInstance().signOut();
    }
}
