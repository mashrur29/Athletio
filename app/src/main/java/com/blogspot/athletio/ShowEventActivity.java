package com.blogspot.athletio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import receivers.EventReminderReceiver;
import general.Event;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class ShowEventActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    Event event;
    String eventKey;

    TextView titleTextview;
    TextView timeDateTextview;
    TextView descriptionTextview;
    TextView hostNameTextview;
    TextView typeTextview;
    TextView distanceTextview;
    TextView durationTextview;
    TextView statusTextview;
    TextView startTextview;
    TextView stopTextview;
    Button addReminderButton;

    SharedPrefData sharedPrefdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        sharedPrefdata=new SharedPrefData(this);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");

        eventKey =getIntent().getStringExtra("EVENT");
        mDatabase.child(eventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    event=new Event(dataSnapshot.getValue().toString());
                    event.key= eventKey;
                    setupUI();
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateUI() {
        titleTextview.setText(" "+event.title);
        timeDateTextview.setText(event.day.day+"/"+event.day.month+"/"+event.day.year+"  "+String.format("%02d", event.hour)+" : "+String.format("%02d", event.min));
        descriptionTextview.setText(event.description);
        hostNameTextview.setText(event.creatorName);
        if(event.type==0)
            typeTextview.setText("Running");
        else if(event.type==1)
            typeTextview.setText("Cycling");
        else if(event.type==2)
            typeTextview.setText("Football");
        else if(event.type==3)
            typeTextview.setText("Cricket");
        else if(event.type==4)
            typeTextview.setText("Walking");
        else if(event.type==5)
            typeTextview.setText("Other");
        durationTextview.setText(event.durationInSec/60+" min "+event.durationInSec%60+"sec");
        if(event.getStatus()==0){
            statusTextview.setText("Active");
        }
        else if(event.getStatus()==1){
            statusTextview.setText("Running");
        }
        else if(event.getStatus()==2){
            statusTextview.setText("Cancelled");
        }
        else if(event.getStatus()==3){
            statusTextview.setText("Finished");
        }
        if(event.type==0||event.type==1||event.type==4){
            distanceTextview.setText(event.distanceInMeters+" m");
        }
        else {
            distanceTextview.setText("Not Available");
        }
        if(sharedPrefdata.hasEventReminderKey(eventKey))
            addReminderButton.setText("Cancel Reminder");
        else
            addReminderButton.setText("Add Reminder");
    }

    private void setupUI() {
        titleTextview =  (TextView) findViewById(R.id.show_event_layout_title);
        timeDateTextview = (TextView)  findViewById(R.id.show_event_layout_time_textview);
        descriptionTextview = (TextView)  findViewById(R.id.show_event_layout_event_description_textview);
        hostNameTextview = (TextView)findViewById(R.id.show_event_layout_host_name_textview);
        typeTextview = (TextView) findViewById(R.id.show_event_layout_event_type_textview);
        distanceTextview = (TextView) findViewById(R.id.show_event_layout_event_distance_textview);
        durationTextview = (TextView) findViewById(R.id.show_event_layout_event_duration_textview);
        statusTextview = (TextView) findViewById(R.id.show_event_layout_event_status_textview);
        startTextview = (TextView) findViewById(R.id.show_event_layout_event_start_loc_textview);
        stopTextview = (TextView) findViewById(R.id.show_event_layout_event_stop_loc_textview);
        addReminderButton =(Button)findViewById(R.id.show_event_layout_add_reminder_button);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                toogleAlarmOnOff(2);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void toogleAlarmOnOff(int minutuesBefore){
        if(!sharedPrefdata.hasEventReminderKey(eventKey)){

            Calendar cal = Calendar.getInstance();
            int mincount=event.min-cal.get(Calendar.MINUTE);
            int hourcount=event.hour-cal.get(Calendar.HOUR_OF_DAY);
            int daycount=event.day.day-cal.get(Calendar.DAY_OF_MONTH);
            int monthcount=(event.day.month-1)-cal.get(Calendar.MONTH);
            int yearcount=event.day.year-cal.get(Calendar.YEAR);
            cal.set(Calendar.SECOND,0);
            cal.add(Calendar.MINUTE,mincount);
            cal.add(Calendar.HOUR_OF_DAY,hourcount);
            cal.add(Calendar.DAY_OF_MONTH,daycount);
            cal.add(Calendar.MONTH,monthcount);
            cal.add(Calendar.YEAR,yearcount);
            cal.add(Calendar.MINUTE,-minutuesBefore);
            if(event.getStatus()==Event.FINISHED)
            {
                Toast.makeText(ShowEventActivity.this,"Event is FINISHED",Toast.LENGTH_SHORT).show();
                return;
            }
            if(event.getStatus()==Event.RUNNING)
            {
                Toast.makeText(ShowEventActivity.this,"Event is Running",Toast.LENGTH_SHORT).show();
                return;
            }
            if(event.getStatus()==Event.CANCELLED)
            {
                Toast.makeText(ShowEventActivity.this,"Event is Cancelled",Toast.LENGTH_SHORT).show();
                return;
            }


            int id=sharedPrefdata.getEventReminderKey(SharedPrefData.LATEST)+1;
            sharedPrefdata.saveEventReminderKey(SharedPrefData.LATEST,id);
            sharedPrefdata.saveEventReminderKey(eventKey,id);
            addEventAlarm(event.title,"Im here",id,cal, eventKey);
        }
        else{
            cancelEventAlarm(sharedPrefdata.getEventReminderKey(eventKey));
            sharedPrefdata.removeEventKey(eventKey);
        }
        updateUI();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void addEventAlarm(String title, String note, int reqid, Calendar calendar,String key){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(ShowEventActivity.this, EventReminderReceiver.class);
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("titleTextview", title);
        notificationIntent.putExtra("notes", note);
        notificationIntent.putExtra("id", reqid);
        notificationIntent.putExtra("event", key);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, reqid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
        Toast.makeText(ShowEventActivity.this,"Reminder Added",Toast.LENGTH_LONG).show();

    }

    void cancelEventAlarm(int reqid){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(ShowEventActivity.this,EventReminderReceiver.class);
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, reqid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        broadcast.cancel();

        Toast.makeText(ShowEventActivity.this,"Reminder Cancelled",Toast.LENGTH_LONG).show();
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
