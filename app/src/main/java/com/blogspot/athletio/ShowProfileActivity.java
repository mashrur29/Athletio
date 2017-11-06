package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import general.User;
import general.myProgressValues;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;


///Shows profile of a person

public class ShowProfileActivity extends AppCompatActivity {
    public String UID , PhotoID;
    TextView profileNameTextView , weightTextView , heightTextView ;
    TextView  genderTextView , birthdayTextView , emailTextView;
    Button weightButton , caloryBurntButton , stepCountButton;
    ImageView profileNameImageView;
    public DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        UID=getIntent().getStringExtra("UID");
        PhotoID = getIntent().getStringExtra("PhotoId");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setupUI();
        mDatabase.child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    User user = new User(dataSnapshot.getValue().toString());
                    updateUI(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateUI(User use) {
        User user = use;
        Picasso.with(this)
                .load(PhotoID)
                .resize(200, 200)
                .into(profileNameImageView);
        profileNameTextView.setText(user.userInfo.displayName);
        weightTextView.setText("Weight: "+user.userData.getWeight());
        heightTextView.setText("Height: "+user.userData.getHeight());
        genderTextView.setText("Gender: "+user.userInfo.getGender());
        birthdayTextView.setText("Birthday: "+user.userInfo.getBirthday());
        emailTextView.setText("Email: "+user.userInfo.getEmail());
        weightButton.setText("Weight Chart");
        stepCountButton.setText("StepCount Chart");
        caloryBurntButton.setText("CaloryBurnt Chart");
        myProgressValues.stepCountProgressHashMap = user.userData.stepCountMap;
        myProgressValues.caloryBurntProgressHashMap = user.userData.calorieMap;
        myProgressValues.weightProgressHashMap = user.userData.weightMap;
        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowProfileActivity.this, MyWeightProgressStatsActivity.class));
            }
        });
        stepCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowProfileActivity.this , MyStepCountProgressStatsActivity.class));
            }
        });
        caloryBurntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowProfileActivity.this, MyCaloryBurntStatsActivity.class));
            }
        });
    }

    private void setupUI() {
        profileNameImageView = (ImageView)findViewById(R.id.show_profile_imageview);
        profileNameTextView = (TextView) findViewById(R.id.show_profile_name_textview);
        weightTextView = (TextView) findViewById(R.id.show_profile_weight_textview);
        heightTextView = (TextView) findViewById(R.id.show_profile_height_textview);
        birthdayTextView = (TextView) findViewById(R.id.show_profile_birthday_textview);
        genderTextView = (TextView) findViewById(R.id.show_profile_gender_textview);
        emailTextView = (TextView) findViewById(R.id.show_profile_email_textview);
        weightButton = (Button) findViewById(R.id.show_profile_weight_button);
        stepCountButton = (Button) findViewById(R.id.show_profile_stepcount_button);
        caloryBurntButton = (Button) findViewById(R.id.show_profile_caloryburnt_button);
        profileNameTextView.setText("Athletio user's Name");
        weightTextView.setText("Athletio user's Weight");
        heightTextView.setText("Athletio user's Height");
        genderTextView.setText("Athletio user's Gender");
        birthdayTextView.setText("Athletio user's Birthday");

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