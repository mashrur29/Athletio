package com.blogspot.athletio;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import general.Day;
import general.Event;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;
///Allows User to create an event
public class CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final int STARTLATREQ=0;
    public static final int STOPLATREQ=1;

    DatabaseReference mDatabase,mUserDatabase;
    FirebaseAuth mAuth;

    LatLng startLatLng =new LatLng(0.0,0.0);
    LatLng stopLatLng =new LatLng(0.0,0.0);
    int eventYear,eventMonth,eventDay,eventHour,eventMin,eventType;

    EditText eventTitleEdittext,eventDurationEdittext,eventDistanceEdittext,eventDescriptionEdittext;
    Button submitButton, choooseStartLocationButton, choooseStopLocationButton;
    Spinner eventTypeSpinner,eventHourSpinner,eventMinSpinner,eventDaySpinner,eventMonthSpinner,eventYearSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        setupUI();

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");
        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");


    }

    private void setupUI() {
        eventTypeSpinner=(Spinner)findViewById(R.id.create_event_layout_type_spinner);
        eventHourSpinner=(Spinner)findViewById(R.id.create_event_start_hours_spinner);
        eventMinSpinner=(Spinner)findViewById(R.id.create_event_start_minutes_spinner);
        eventDaySpinner=(Spinner)findViewById(R.id.create_event_layout_day_spinner);
        eventMonthSpinner=(Spinner)findViewById(R.id.create_event_layout_month_spinner);
        eventYearSpinner=(Spinner)findViewById(R.id.create_event_layout_year_spinner);
        addItemOnEventTypeSpinner();
        addItemOnEventHoursSpinner();
        addItemOnEventMinSpinner();
        addItemOnEventDaySpinner();
        addItemOnEventMonthSpinner();
        addItemOnEventYearSpinner();
        eventTypeSpinner.setOnItemSelectedListener(this);
        eventHourSpinner.setOnItemSelectedListener(this);
        eventMinSpinner.setOnItemSelectedListener(this);
        eventDaySpinner.setOnItemSelectedListener(this);
        eventMonthSpinner.setOnItemSelectedListener(this);
        eventYearSpinner.setOnItemSelectedListener(this);
        eventTitleEdittext=(EditText)findViewById(R.id.create_event_title_edit_text);
        eventDurationEdittext=(EditText)findViewById(R.id.create_event_layout_duration_edit_text);
        eventDistanceEdittext=(EditText)findViewById(R.id.create_event_layout_distance_edit_text);
        eventDescriptionEdittext=(EditText)findViewById(R.id.create_event_layout_description_edit_text);
        submitButton =(Button)findViewById(R.id.create_event_layout_submit_bt);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Integer.parseInt(eventDurationEdittext.getText().toString());
                }catch (Exception e){
                    Toast.makeText(CreateEventActivity.this,"Enter Duration Correctly",Toast.LENGTH_LONG).show();
                    return;
                }
                if(eventType==Event.RUNTYPE||eventType==Event.WALKTYPE||eventType==Event.CYCLINGTYPE){

                    try{
                        Integer.parseInt(eventDistanceEdittext.getText().toString());
                    }catch (Exception e){
                        Toast.makeText(CreateEventActivity.this,"Enter Distance Correctly",Toast.LENGTH_LONG).show();
                        return;
                    }
                    createEvent(new Day(eventDay,eventMonth,eventYear),eventHour,eventMin, startLatLng, stopLatLng, eventType,Integer.parseInt(eventDistanceEdittext.getText().toString()),Integer.parseInt(eventDurationEdittext.getText().toString())*60,eventTitleEdittext.getText().toString(),eventDescriptionEdittext.getText().toString());

                }
                else {
                    createEvent(new Day(eventDay,eventMonth,eventYear),eventHour,eventMin, startLatLng,Event.FOOTBALLTYPE,Integer.parseInt(eventDurationEdittext.getText().toString())*60,eventTitleEdittext.getText().toString(),eventDescriptionEdittext.getText().toString());
                }
                finish();

            }
        });
        choooseStartLocationButton =(Button)findViewById(R.id.create_event_layout_choose_start_latbt);
        choooseStartLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStartLat();
            }
        });
        choooseStopLocationButton =(Button)findViewById(R.id.create_event_layout_chooses_stop_latbt);
        choooseStopLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStopLat();
            }
        });
    }

    ///Calls to get event stop location from map
    private void chooseStopLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STOPLATREQ);
    }

    ///Calls to get event start location from map
    private void chooseStartLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STARTLATREQ);
    }
    ///Uploads event to online database
    private void createEvent(Day day,int hour,int min,LatLng start,LatLng stop, int type, double distanceInMeters, long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,stop,type,distanceInMeters,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }
    ///Uploads event to online database
    private void createEvent(Day day,int hour,int min,LatLng start, int type,  long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,type,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STARTLATREQ) {
            this.startLatLng =new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got", startLatLng.toString());
        }
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STOPLATREQ) {
            this.stopLatLng =new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got", stopLatLng.toString());
        }
    }
    public void addItemOnEventTypeSpinner()
    {
        List<String> types = new ArrayList<String>();
        types.add("Running");
        types.add("Cycling");
        types.add("Football");
        types.add("Cricket");
        types.add("Walking");
        types.add("Others");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner typeSpinner = (Spinner) findViewById(R.id.create_event_layout_type_spinner);
        typeSpinner.setAdapter(dataAdapter);
        return;
    }
    public void addItemOnEventHoursSpinner()
    {
        List<Integer> hour = new ArrayList<Integer>();
        for(int i=0;i<24;i++)hour.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, hour);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner hourSpinner = (Spinner) findViewById(R.id.create_event_start_hours_spinner);
        hourSpinner.setAdapter(dataAdapter);
        return;
    }
    public void addItemOnEventMinSpinner()
    {
        List<Integer> min = new ArrayList<Integer>();
        for(int i=0;i<60;i++)min.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, min);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner minSpinner = (Spinner) findViewById(R.id.create_event_start_minutes_spinner);
        minSpinner.setAdapter(dataAdapter);
        return;
    }
    private void addItemOnEventDaySpinner() {
        List<Integer> days = new ArrayList<Integer>();
        for(int i=0;i<31;i++)days.add(i+1);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, days);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner daySpinner = (Spinner) findViewById(R.id.create_event_layout_day_spinner);
        daySpinner.setAdapter(dataAdapter);
        return;
    }
    private void addItemOnEventMonthSpinner() {
        List<Integer> months = new ArrayList<Integer>();
        for(int i=1;i<13;i++)months.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, months);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner monthSpinner = (Spinner) findViewById(R.id.create_event_layout_month_spinner);
        monthSpinner.setAdapter(dataAdapter);
        return;
    }

    private void addItemOnEventYearSpinner() {
        List<Integer> years = new ArrayList<Integer>();
        for(int i=2017;i<2055;i++)years.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner yearSpinner = (Spinner) findViewById(R.id.create_event_layout_year_spinner);
        yearSpinner.setAdapter(dataAdapter);
        return;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.create_event_layout_type_spinner)
        {
            if(item.equals("Running")){
                eventType=Event.RUNTYPE;
            }
            else if(item.equals("Cycling")){
                eventType=Event.CYCLINGTYPE;
            }
            else if(item.equals("Football")){
                eventType=Event.FOOTBALLTYPE;
            }
            else if(item.equals("Cricket")){
                eventType=Event.CRICKETTYPE;
            }
            else if(item.equals("Walking")) {
                eventType=Event.WALKTYPE;
            }
            else {
                eventType=Event.OTHERTYPE;
            }
        }
        else if(spinner.getId() == R.id.create_event_start_hours_spinner)
        {
            eventHour =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.create_event_start_minutes_spinner)
        {
            eventMin =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.create_event_layout_day_spinner)
        {
            eventDay =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.create_event_layout_month_spinner)
        {
            eventMonth =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.create_event_layout_year_spinner)
        {
            eventYear =Integer.parseInt(item);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
