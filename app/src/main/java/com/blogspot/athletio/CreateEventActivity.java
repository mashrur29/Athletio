package com.blogspot.athletio;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    public static final int STARTLATREQ=0;
    public static final int STOPLATREQ=1;


    DatabaseReference mDatabase,mUserDatabase;
    FirebaseAuth mAuth;

    LatLng startlatLng=new LatLng(0.0,0.0);
    LatLng stoplatLng=new LatLng(0.0,0.0);


    Button bt,startbt,stopbt;

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
                createEvent(new Day(),12,13,startlatLng,stoplatLng,Event.RUNTYPE,200,1000,"wedtyr","ryrtwed");
                createEvent(new Day(),12,13,startlatLng,Event.FOOTBALLTYPE,1000,"weod","dweryrd");

            }
        });
        startbt=(Button)findViewById(R.id.createeventchoosestartlatbt);
        startbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStartLat();
            }
        });
        stopbt=(Button)findViewById(R.id.createeventchoosestoplatbt);
        stopbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseStopLat();
            }
        });
    }

    private void chooseStopLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STOPLATREQ);
    }

    private void chooseStartLat() {
        startActivityForResult(new Intent(CreateEventActivity.this,ChooseLatFromMapActivity.class),CreateEventActivity.STARTLATREQ);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STARTLATREQ) {
            this.startlatLng=new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got",startlatLng.toString());
        }
        if (resultCode == RESULT_OK && requestCode == CreateEventActivity.STOPLATREQ) {
            this.stoplatLng=new LatLng(data.getExtras().getDouble("lat"),data.getExtras().getDouble("lng"));
            Log.d("got",stoplatLng.toString());
        }
    }
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(CreateEventActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.d("IGA", "Address" + add);
        } catch (IOException e) {

        }
    }

}
