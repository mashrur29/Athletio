package com.blogspot.athletio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import general.Event;
import storage.SharedPrefData;

public class ShowEventActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    Event event;
    String key;



    TextView title;
    TextView timeDate;
    TextView description;
    TextView hostName;
    TextView type;
    TextView distance;
    TextView duration;
    TextView status;
    TextView start;
    TextView stop;
    Button bt;

    SharedPrefData sharedPrefdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        sharedPrefdata=new SharedPrefData(this);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");

        key=getIntent().getStringExtra("EVENT");
        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    event=new Event(dataSnapshot.getValue().toString());
                    event.key=key;
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
        title.setText(" "+event.title);
        timeDate.setText(event.day.day+"/"+event.day.month+"/"+event.day.year+"  "+String.format("%02d", event.hour)+" : "+String.format("%02d", event.min));
        description.setText(event.description);
        hostName.setText(event.creatorName);
        if(event.type==0)
            type.setText("Running");
        else if(event.type==1)
            type.setText("Cycling");
        else if(event.type==2)
            type.setText("Football");
        else if(event.type==3)
            type.setText("Cricket");
        else if(event.type==4)
            type.setText("Walking");
        else if(event.type==5)
            type.setText("Other");
        duration.setText(event.durationInSec/60+" min "+event.durationInSec%60+"sec");
        if(event.getStatus()==0){
            status.setText("Active");
        }
        else if(event.getStatus()==1){
            status.setText("Running");
        }
        else if(event.getStatus()==2){
            status.setText("Cancelled");
        }
        else if(event.getStatus()==3){
            status.setText("Finished");
        }
        if(event.type==0||event.type==1||event.type==4){
            distance.setText(event.distanceInMeters+" m");
        }
        else {
            distance.setText("Not Available");
        }
        if(sharedPrefdata.hasEventReminderKey(key))
            bt.setText("Cancel Reminder");
        else
            bt.setText("Add Reminder");
    }

    private void setupUI() {
        title =  (TextView) findViewById(R.id.showeventTitle);
        timeDate = (TextView)  findViewById(R.id.showeventTimeDate);
        description = (TextView)  findViewById(R.id.showeventDescription);
        hostName = (TextView)findViewById(R.id.showeventHostName);
        type = (TextView) findViewById(R.id.showeventType);
        distance= (TextView) findViewById(R.id.showeventDistance);
        duration= (TextView) findViewById(R.id.showeventDuration);
        status= (TextView) findViewById(R.id.showeventStatus);
        start= (TextView) findViewById(R.id.showeventstartloction);
        stop= (TextView) findViewById(R.id.showeventEndloction);
        bt=(Button)findViewById(R.id.showeventbt);
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                toogleAlarm(2);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void toogleAlarm(int minutuesBefore){
        if(!sharedPrefdata.hasEventReminderKey(key)){

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
            sharedPrefdata.saveEventReminderKey(key,id);
            addEventAlarm(event.title,"Im here",id,cal,key);
        }
        else{
            cancelEventAlarm(sharedPrefdata.getEventReminderKey(key));
            sharedPrefdata.removeEventKey(key);
        }
        updateUI();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void addEventAlarm(String title, String note, int reqid, Calendar calendar,String key){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(ShowEventActivity.this,EventReminderReceiver.class);
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        notificationIntent.putExtra("title", title);
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


}
