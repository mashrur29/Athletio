package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class ShowEventRemindersActivity extends AppCompatActivity {
    SharedPrefData sharedPrefData;
    DatabaseReference mDatabase;
    Vector<String> keys;

    Vector<Event> events;

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event_reminder);

        sharedPrefData=new SharedPrefData(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");
        keys=sharedPrefData.getEventReminderKeys();
        events=new Vector<Event>();
        for (String key:keys){
            mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event event=new Event(dataSnapshot.getValue().toString());
                    event.key=dataSnapshot.getKey().toString();
                    events.add(event);

                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        setupUI();


    }

    private void updateUI() {
        tv.setText(events.toString());
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.showeventremindertv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(events.get(0)!=null)
                    showEvent(events.get(0).key);
            }
        });
    }
    void showEvent(String event){
        Intent intent=new Intent(ShowEventRemindersActivity.this,ShowEventActivity.class);
        intent.putExtra("EVENT",event);
        startActivity(intent);
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
        Intent intent=new Intent(this,FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(this,StepDetector.class);
        stopService(intent2);
        FirebaseAuth.getInstance().signOut();
    }
}
