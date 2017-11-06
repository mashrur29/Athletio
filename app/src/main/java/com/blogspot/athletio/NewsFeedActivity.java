package com.blogspot.athletio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import adapters.PostListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import general.Post;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class NewsFeedActivity extends Activity {
    private static final String TAG = NewsFeedActivity.class.getSimpleName();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = ref.child("NewsFeedUser");
    Map<String, Post> users = new HashMap<>();
    Button createPostButton, searchButton, messengerButton;

    private ListView listView;

    private PostListAdapter listAdapter;
    ;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        createPostButton = (Button) findViewById(R.id.news_feed_new_post);
        listView = (ListView) findViewById(R.id.news_feed_listview);
        searchButton = (Button) findViewById(R.id.news_feed_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this , ShowUserListActivity.class));
            }
        });

        messengerButton = (Button) findViewById(R.id.news_feed_msg_button);
        messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeedActivity.this , OnlineChatLogin.class));
            }
        });

        posts = new ArrayList<Post>();
        listAdapter = new PostListAdapter(this, posts);
        listView.setAdapter(listAdapter);
        FirebaseDatabase.getInstance().getReference().child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Post post = new Post(d.getValue().toString());
                    posts.add(post);
                }
                Collections.reverse(posts);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsFeedActivity.this, PostPublishActivity.class);
                startActivity(intent);
            }
        });
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
                startActivity(new Intent(this, NewsFeedActivity.class));
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

