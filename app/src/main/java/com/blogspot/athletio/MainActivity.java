package com.blogspot.athletio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import general.Day;
import general.SmallStep;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;


public class MainActivity extends AppCompatActivity {
    Context context;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    TextView callorieTextview, stepCountTextview;
    Button statsButton, rankButton;

    Thread viewThread;
    boolean viewThreadRunning = true;

    HashMap<String, SmallStep> rankMap = new HashMap<String, SmallStep>();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        setupUI();

        mAuth = FirebaseAuth.getInstance();

        updateUI();
        viewThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted() && viewThreadRunning) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        viewThread.start();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };
        FirebaseDatabase.getInstance().getReference().child("Steps").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        SmallStep smallStep = new SmallStep(d.getValue().toString());
                        rankMap.put(d.getKey(), smallStep);

                    }
                    Log.d("log", rankMap.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setupUI() {
        callorieTextview = (TextView) findViewById(R.id.main_layout_callorie_textview);
        stepCountTextview = (TextView) findViewById(R.id.main_layout_steps_textview);
        statsButton = (Button) findViewById(R.id.main_layout_stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyProgressStatsActivity.class));
            }
        });
        rankButton = (Button) findViewById(R.id.main_layout_rank_button);
        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RankActivity.class));
            }
        });



    }

    void updateUI() {

        SharedPreferences calorieMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        callorieTextview.setText("" + calorieMapPref.getInt(new Day().toString(), 0));
        SharedPreferences stepCountMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        stepCountTextview.setText("" + stepCountMapPref.getInt(new Day().toString(), 0));


    }

    void signOut() {
        viewThreadRunning = false;
        SharedPrefData sharedPrefData = new SharedPrefData(MainActivity.this);
        sharedPrefData.clear();
        Intent intent = new Intent(MainActivity.this, FirebaseUploadService.class);
        stopService(intent);

        Intent intent2 = new Intent(MainActivity.this, StepDetector.class);
        stopService(intent2);
        mAuth.signOut();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewThreadRunning = false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menu_track_workout:
                startActivity(new Intent(this, TrackWorkoutMenuActivity.class));
                return true;
            case R.id.menu_online_workout:
                startActivity(new Intent(this, OnlineWorkoutActivity.class));
                return true;
            case R.id.menu_my_workouts:
                startActivity(new Intent(this, MyWorkoutsActivity.class));
                return true;
            case R.id.menu_excersices:
                startActivity(new Intent(this, ExercisesActivity.class));
                return true;
            case R.id.menu_social:
                startActivity(new Intent(this, SocialMainActivity.class));
                return true;
            case R.id.menu_events:
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            case R.id.menu_event_reminder:
                startActivity(new Intent(this, ShowEventRemindersActivity.class));
                return true;
            case R.id.menu_create_event:
                startActivity(new Intent(this, CreateEventActivity.class));
                return true;
            case R.id.menu_nearby_place:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.menu_chat_bot:
                startActivity(new Intent(this, ChatBotMain.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
