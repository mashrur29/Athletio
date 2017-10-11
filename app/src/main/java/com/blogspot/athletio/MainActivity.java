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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Context context;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //rem
    TextView displayName,weight,callorie,stepCount,height;
    Button stat;
    //end rem



    Thread t;
    boolean b=true;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context=this;
        setupUI();

        mAuth=FirebaseAuth.getInstance();

        updateUI();
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()&&b) {
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

        t.start();
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
        displayName=(TextView)findViewById(R.id.mndisplayname);
        weight=(TextView)findViewById(R.id.mnweight);
        callorie=(TextView)findViewById(R.id.mncallorie);
        stepCount=(TextView)findViewById(R.id.mnstepcount);
        height=(TextView)findViewById(R.id.mnheight);
        stat=(Button)findViewById(R.id.mnstat);
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyStatsActivity.class));
            }
        });




    }

    void updateUI(){

        SharedPreferences pref = MainActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
        displayName.setText(pref.getString(SharedPrefData.DISPLAYNAME,""));
        weight.setText("weight: "+pref.getInt(SharedPrefData.WEIGHT,0));
        SharedPreferences calorieMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        callorie.setText("callorie:"+calorieMapPref.getInt(new Day().toString(),0));
        SharedPreferences stepCountMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        stepCount.setText("Stepcount: "+stepCountMapPref.getInt(new Day().toString(),0));
        height.setText("height: "+pref.getInt(SharedPrefData.HEIGHT,0));


    }

    void signOut(){
        b=false;
        SharedPrefData sharedPrefData=new SharedPrefData(MainActivity.this);
        sharedPrefData.clear();
        Intent intent=new Intent(MainActivity.this,FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(MainActivity.this,StepDetector.class);
        stopService(intent2);
        mAuth.signOut();
    }

 

    @Override
    protected void onDestroy() {
        super.onDestroy();
        b=false;
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
