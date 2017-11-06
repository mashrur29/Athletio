package com.blogspot.athletio;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;
///Activity allows user to choose the type of workout they want to track
public class TrackWorkoutMenuActivity extends AppCompatActivity {
    Button runningButton, cyclingButton, treadmillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout_menu);
        setupUI();
    }

    private void setupUI() {
        runningButton =(Button)findViewById(R.id.track_workout_layout_running_button);
        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackWorkoutMenuActivity.this,RunningTrackActivity.class));
            }
        });
        cyclingButton =(Button)findViewById(R.id.track_workout_layout_cycling_button);
        cyclingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackWorkoutMenuActivity.this,CyclingTrackActivity.class));
            }
        });
        treadmillButton =(Button)findViewById(R.id.track_workout_layout_treadmill_vr_button);
        treadmillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(TrackWorkoutMenuActivity.this,"com.blogspot.athelioappvr","AthelioVR");
            }
        });

    }


    ///Opens Treadmill VR app
    public static boolean openApp(Context context, String packageName, String appname){
        PackageManager manager=context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if(i==null)
        {
            Toast.makeText(context,"Please install "+appname,Toast.LENGTH_LONG).show();
            return false;

        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;

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
