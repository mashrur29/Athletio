package com.blogspot.athletio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import adapters.RankAdapter;
import views.DividerItemDecoration;
import listeners.RecyclerTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import general.RankPersons;
import general.User;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;


public class RankActivity extends AppCompatActivity {
    //here overload comparator
    public Comparator<RankPersons> rankPersonsComparator
            = new Comparator<RankPersons>() {

        public int compare(RankPersons persons1, RankPersons persons2) {
            if (persons1.getSteps().equals(persons2.getSteps())) {
                return persons1.getName().compareTo(persons2.getName());
            } else
                return Integer.parseInt(persons2.getSteps()) - Integer.parseInt(persons1.getSteps());
        }

    };
    private List<RankPersons> rankPersonsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RankAdapter mAdapter;
    private HashMap<String, Integer> stepHashMap, caloryHashMap;
    EditText searchEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        recyclerView = (RecyclerView) findViewById(R.id.rank_contain_main_recycler_view);
        searchEditView = (EditText) findViewById(R.id.rank_search_editview);
        mAdapter = new RankAdapter(rankPersonsList);
        searchEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                RankActivity.this.mAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RankActivity.this.mAdapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

                RankActivity.this.mAdapter.getFilter().filter(s);
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RankPersons rankPersons = rankPersonsList.get(position);
                Toast.makeText(getApplicationContext(), rankPersons.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        initRankData();
        Collections.sort(rankPersonsList, rankPersonsComparator);
    }

    private void initRankData() {
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User user = new User(d.getValue().toString());
                    String rankPersonName = user.getUserInfo().getDisplayName();
                    String rankPersonWeight = String.valueOf(user.getUserData().getWeight());
                    caloryHashMap = user.getUserData().calorieMap;
                    stepHashMap = user.getUserData().getCalorieMap();
                    String rankPersonStep = calTotalStep();
                    String rankPersonCalory = calTotalCalory();
                    RankPersons rankPersons = new RankPersons(rankPersonName, rankPersonStep, rankPersonCalory, rankPersonWeight);
                    rankPersonsList.add(rankPersons);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mAdapter.notifyDataSetChanged();
    }

    String calTotalStep() {
        long sumStep = 0;
        for (HashMap.Entry<String, Integer> iterator : stepHashMap.entrySet()) {
            sumStep = sumStep + iterator.getValue();
        }
        return String.valueOf(sumStep);
    }

    String calTotalCalory() {
        long sumCalory = 0;
        for (HashMap.Entry<String, Integer> iterator : caloryHashMap.entrySet()) {
            sumCalory = sumCalory + iterator.getValue();
        }
        return String.valueOf(sumCalory);
    }

    public void filter(String text) {
        if(text!=null) {
            List<RankPersons> temp = new ArrayList();
            for (RankPersons d : rankPersonsList) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getName().contains(text)) {
                    temp.add(d);
                }
            }
            new RankAdapter(temp);
        }
        else
        {
            new RankAdapter(rankPersonsList);
        }
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
