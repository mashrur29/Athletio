package com.blogspot.athletio;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import general.Day;
import general.Event;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class CreateEventActivity extends AppCompatActivity {

    public static final int STARTLATREQ=0;
    public static final int STOPLATREQ=1;


    DatabaseReference mDatabase,mUserDatabase;
    FirebaseAuth mAuth;

    LatLng startlatLng=new LatLng(0.0,0.0);
    LatLng stoplatLng=new LatLng(0.0,0.0);


    Button bt,startbt,stopbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setupUI();

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");


    }

    private void setupUI() {
        bt=(Button)findViewById(R.id.create_event_submit_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(new Day(),12,13,startlatLng,stoplatLng, Event.RUNTYPE,200,1000,"wedtyr","ryrtwed");
                createEvent(new Day(),12,13,startlatLng,Event.FOOTBALLTYPE,1000,"weod","dweryrd");

            }
        });
        startbt=(Button)findViewById(R.id.create_event_choose_start_latbt);
        startbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStartLat();
            }
        });
        stopbt=(Button)findViewById(R.id.create_event_chooses_stop_latbt);
        stopbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStopLat();
            }
        });
    }

    private void chooseStopLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STOPLATREQ);
    }

    private void chooseStartLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STARTLATREQ);
    }

    private void createEvent(Day day,int hour,int min,LatLng start,LatLng stop, int type, double distanceInMeters, long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,stop,type,distanceInMeters,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }
    private void createEvent(Day day,int hour,int min,LatLng start, int type,  long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,type,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STARTLATREQ) {
            this.startlatLng=new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got",startlatLng.toString());
        }
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STOPLATREQ) {
            this.stoplatLng=new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got",stoplatLng.toString());
        }
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(CreateEventActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        } catch (IOException e) {

        }
        return null;
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
