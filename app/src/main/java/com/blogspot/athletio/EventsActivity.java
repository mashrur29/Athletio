package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Vector;

public class EventsActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    Vector<Event> events=new Vector<Event>();

    //
    TextView tv;
   //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");

        setupUI();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        Event event=new Event(d.getValue().toString());
                        event.key=d.getKey();
                        events.add(event);
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
        tv.setText(events.toString());
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.eventstv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(events.get(0)!=null){
                    showEvent(events.get(0).key.toString());
                }
            }
        });
    }

    void showEvent(String event){
        Intent intent=new Intent(EventsActivity.this,ShowEventActivity.class);
        intent.putExtra("EVENT",event);
        startActivity(intent);
    }
}
