package com.blogspot.athletio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import general.Day;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;


public class MainActivity extends AppCompatActivity {
    Context context;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    TextView callorieTextview, stepCountTextview;

    Thread viewThread;
    boolean viewThreadRunning =true;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        setupUI();

        mAuth=FirebaseAuth.getInstance();

        updateUI();
        viewThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()&& viewThreadRunning) {
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
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
            }
        };
    }

    void setupUI(){
        callorieTextview =(TextView)findViewById(R.id.main_layout_callorie_textview);
        stepCountTextview =(TextView)findViewById(R.id.main_layout_steps_textview);


    }

    void updateUI(){

        SharedPreferences calorieMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        callorieTextview.setText(""+calorieMapPref.getInt(new Day().toString(),0));
        SharedPreferences stepCountMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        stepCountTextview.setText(""+stepCountMapPref.getInt(new Day().toString(),0));


    }

    void signOut(){
        viewThreadRunning =false;
        SharedPrefData sharedPrefData=new SharedPrefData(MainActivity.this);
        sharedPrefData.clear();
        Intent intent=new Intent(MainActivity.this, FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(MainActivity.this, StepDetector.class);
        stopService(intent2);
        mAuth.signOut();
    }

 

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewThreadRunning =false;
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
}
