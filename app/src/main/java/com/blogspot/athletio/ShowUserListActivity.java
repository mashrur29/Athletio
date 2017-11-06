package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import adapters.SmallUserCardAdapter;
import general.SmallUser;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

///Allows to search for presons and visit their profile

public class ShowUserListActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText userListSearchEditTextView;
    List<SmallUser> smallUsers;
    RecyclerView recyclerView;
    private SmallUserCardAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_list);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Userlist");
        smallUsers=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.show_user_list_layout_card_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        mAdapter = new SmallUserCardAdapter(smallUsers);
        recyclerView.setAdapter(mAdapter);
        userListSearchEditTextView = (EditText) findViewById(R.id.user_list_search_editview);
        userListSearchEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ShowUserListActivity.this.mAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ShowUserListActivity.this.mAdapter.getFilter().filter(s);
            }


            @Override
            public void afterTextChanged(Editable s) {

                ShowUserListActivity.this.mAdapter.getFilter().filter(s);
            }
        });
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
        mAdapter = new SmallUserCardAdapter(smallUsers);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupUI() {
        userListSearchEditTextView = (EditText) findViewById(R.id.user_list_search_editview);

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
