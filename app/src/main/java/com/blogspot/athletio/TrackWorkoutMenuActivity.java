package com.blogspot.athletio;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TrackWorkoutMenuActivity extends AppCompatActivity {
    Button runningbt,cyclingbt,treadmillbt,othersbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout_menu);
        setupUI();
    }

    private void setupUI() {
        runningbt=(Button)findViewById(R.id.trackworkoutmenurunningbt);
        runningbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackWorkoutMenuActivity.this,RunningTrackActivity.class));
            }
        });
        cyclingbt=(Button)findViewById(R.id.trackworkoutmenucyclingbt);
        cyclingbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackWorkoutMenuActivity.this,CyclingTrackActivity.class));
            }
        });
        treadmillbt=(Button)findViewById(R.id.trackworkoutmenutreadmillvrbt);
        treadmillbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(TrackWorkoutMenuActivity.this,"com.blogspot.athelioappvr","AthelioVR");
            }
        });

    }

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
}
