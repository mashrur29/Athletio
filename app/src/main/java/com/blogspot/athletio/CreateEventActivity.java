package com.blogspot.athletio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateEventActivity extends AppCompatActivity {

    DatabaseReference mDatabase,mUserDatabase;
    FirebaseAuth mAuth;




    Button bt;

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
        bt=(Button)findViewById(R.id.createeventbt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent(new Day(),12,13,new LatLng(1.3,2.2),new LatLng(1.3,2.2),Event.RUNTYPE,200,1000,"wedtyr","ryrtwed");
                createEvent(new Day(),12,13,new LatLng(1.3,2.2),Event.FOOTBALLTYPE,1000,"weod","dweryrd");

            }
        });
    }
    private void createEvent(Day day,int hour,int min,LatLng start,LatLng stop, int type, double distanceInMeters, long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,stop,type,distanceInMeters,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }
    private void createEvent(Day day,int hour,int min,LatLng start, int type,  long durationInSec,String title,String description){
        String key=mDatabase.push().getKey();
        mDatabase.child(key).setValue(new Event(day,hour,min,mAuth.getCurrentUser().getUid().toString(),mAuth.getCurrentUser().getDisplayName(),start,type,durationInSec,title,description));
        mUserDatabase.child(mAuth.getCurrentUser().getUid()).child("Events").child(key).setValue(key);

    }
}
