package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class ShowEventRemindersActivity extends AppCompatActivity {
    SharedPrefData sharedPrefData;
    DatabaseReference mDatabase;
    Vector<String> keys;

    Vector<Event> events;

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event_reminder);

        sharedPrefData=new SharedPrefData(this);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");
        keys=sharedPrefData.getEventReminderKeys();
        events=new Vector<Event>();
        for (String key:keys){
            mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event event=new Event(dataSnapshot.getValue().toString());
                    event.key=dataSnapshot.getKey().toString();
                    events.add(event);

                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        setupUI();


    }

    private void updateUI() {
        tv.setText(events.toString());
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.showeventremindertv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(events.get(0)!=null)
                    showEvent(events.get(0).key);
            }
        });
    }
    void showEvent(String event){
        Intent intent=new Intent(ShowEventRemindersActivity.this,ShowEventActivity.class);
        intent.putExtra("EVENT",event);
        startActivity(intent);
    }
}
