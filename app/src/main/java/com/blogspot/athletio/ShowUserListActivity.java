package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Vector;

import adapters.SmallUserCardAdapter;
import general.SmallUser;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class ShowUserListActivity extends AppCompatActivity {
    DatabaseReference mDatabase;

    List<SmallUser> smallUsers;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_list);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Userlist");
        smallUsers=new Vector<SmallUser>();
        recyclerView = (RecyclerView) findViewById(R.id.show_user_list_layout_card_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        setupUI();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                       SmallUser smallUser=new SmallUser(d.getValue().toString());
                        smallUsers.add(smallUser);
                    }
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        recyclerView.setAdapter(new SmallUserCardAdapter(smallUsers));
    }

    private void setupUI() {

    }

    private void showProfile(String uid) {
        Intent intent=new Intent(ShowUserListActivity.this,ShowProfileActivity.class);
        intent.putExtra("UID",uid);
        startActivity(intent);
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this,MainActivity.class));
                return true;
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
