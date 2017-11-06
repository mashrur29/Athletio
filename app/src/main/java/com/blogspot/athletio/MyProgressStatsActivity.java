package com.blogspot.athletio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import general.User;
import general.myProgressValues;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class MyProgressStatsActivity extends AppCompatActivity {
    Button stepCountProgressButton, caloryBurntProgressButton, weightProgressButton;
    private HashMap<String, Integer> hashMapsStep = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashMapCalory = new HashMap<String, Integer>();
    private HashMap<String, Integer> hashMapWeight = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_progress_stats);
        stepCountProgressButton = (Button) findViewById(R.id.progress_layout_step_count_button);
        caloryBurntProgressButton = (Button) findViewById(R.id.progress_layout_calory_burnt_button);
        weightProgressButton = (Button) findViewById(R.id.progress_layout_weight_button);
        initHaspMap();
        myProgressValues.stepCountProgressHashMap = hashMapsStep;
        myProgressValues.caloryBurntProgressHashMap = hashMapCalory;
        myProgressValues.weightProgressHashMap = hashMapWeight;
        stepCountProgressButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyProgressStatsActivity.this, MyStepCountProgressStatsActivity.class);
                startActivity(i);
            }
        });
        caloryBurntProgressButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProgressStatsActivity.this, MyCaloryBurntStatsActivity.class));
            }
        });
        weightProgressButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProgressStatsActivity.this, MyWeightProgressStatsActivity.class));
            }
        });
    }

    void initHaspMap() {
        SharedPrefData sharedPrefData = new SharedPrefData(this);
        User user;
        if (sharedPrefData.getSaved()) {
            user = sharedPrefData.getUser();
            hashMapsStep = user.getUserData().getStepCountMap();
            hashMapCalory = user.getUserData().getCalorieMap();
            hashMapWeight = user.getUserData().getWeightMap();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                return true;
            case R.id.menu_track_workout:
                startActivity(new Intent(this, TrackWorkoutMenuActivity.class));
                finish();
                return true;
            case R.id.menu_online_workout:
                startActivity(new Intent(this, OnlineWorkoutActivity.class));
                finish();
                return true;
            case R.id.menu_my_workouts:
                startActivity(new Intent(this, MyWorkoutsActivity.class));
                finish();
                return true;
            case R.id.menu_excersices:
                startActivity(new Intent(this, ExercisesActivity.class));
                finish();
                return true;
            case R.id.menu_social:
                startActivity(new Intent(this, SocialMainActivity.class));
                finish();
                return true;
            case R.id.menu_events:
                startActivity(new Intent(this, EventsActivity.class));
                finish();
                return true;
            case R.id.menu_event_reminder:
                startActivity(new Intent(this, ShowEventRemindersActivity.class));
                finish();
                return true;
            case R.id.menu_create_event:
                startActivity(new Intent(this, CreateEventActivity.class));
                finish();
                return true;
            case R.id.menu_nearby_place:
                startActivity(new Intent(this, MapsActivity.class));
                finish();
                return true;
            case R.id.menu_chat_bot:
                startActivity(new Intent(this, ChatBotMain.class));
                finish();
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            case R.id.menu_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void signOut() {
        SharedPrefData sharedPrefData = new SharedPrefData(this);
        sharedPrefData.clear();
        Intent intent = new Intent(this, FirebaseUploadService.class);
        stopService(intent);
        Intent intent2 = new Intent(this, StepDetector.class);
        stopService(intent2);
        FirebaseAuth.getInstance().signOut();
    }
}
