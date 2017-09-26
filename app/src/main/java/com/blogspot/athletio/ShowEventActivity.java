package com.blogspot.athletio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ShowEventActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    Event event;
    String key;
    TextView tv;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");

        key=getIntent().getStringExtra("EVENT");
        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event=new Event(dataSnapshot.getValue().toString());

                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setupUI();

    }

    private void updateUI() {
        tv.setText(event.toString());
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.showeventtv);
        bt=(Button)findViewById(R.id.showeventbt);
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 5);
                addEventAlarm("Hi","Im here",1,cal,key);

                Toast.makeText(ShowEventActivity.this,"Notification Added",Toast.LENGTH_LONG).show();
            }
        });
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
    }

    void cancelEventAlarm(int reqid){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(ShowEventActivity.this,EventReminderReceiver.class);
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, reqid, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(broadcast);
        broadcast.cancel();
    }


}
