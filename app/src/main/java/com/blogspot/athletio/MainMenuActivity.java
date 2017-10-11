package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {
    Button trackworkoutbt,socialbt,fitchatbt,settingsbt,exercisessbt,eventbt,createeventbt,myworkoutsbt,showeventsbt,onlineworkoutbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupUI();


        updateUI();
    }

    private void updateUI() {
    }


    private void setupUI() {
        trackworkoutbt=(Button)findViewById(R.id.mainmenutrackworkoutbt);
        trackworkoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,TrackWorkoutMenuActivity.class));
            }
        });
        onlineworkoutbt=(Button)findViewById(R.id.mainmenuonlineworkoutbt);
        onlineworkoutbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,OnlineWorkoutActivity.class));
            }
        });
        exercisessbt=(Button)findViewById(R.id.mainmenueexercisessbt);
        exercisessbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,ExercisesActivity.class));
            }
        });
        socialbt=(Button)findViewById(R.id.mainmenusocialbt);
        socialbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,NewsFeedActivity.class));
            }
        });
        fitchatbt=(Button)findViewById(R.id.mainmenufitchatbt);
        fitchatbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        settingsbt=(Button)findViewById(R.id.mainmenusettingsbt);
        settingsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,SettingsActivity.class));
            }
        });
        eventbt=(Button)findViewById(R.id.mainmenueventbt);
        eventbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,EventsActivity.class));
            }
        });
        createeventbt=(Button)findViewById(R.id.mainmenucreateeventbt);
        createeventbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,CreateEventActivity.class));
            }
        });
        myworkoutsbt=(Button)findViewById(R.id.mainmenumyworkoutsbt);
        myworkoutsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainMenuActivity.this,MyWorkoutsActivity.class));
            }
        });
        showeventsbt=(Button)findViewById(R.id.mainmenushoweventreminderbt);
        showeventsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this,ShowEventRemindersActivity.class));
            }
        });
    }
}
