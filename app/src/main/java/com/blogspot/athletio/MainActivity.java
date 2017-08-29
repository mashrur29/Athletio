package com.blogspot.athletio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //rem
    TextView displayName,weight,callorie,stepCount,height;
    Button mSignOut,stat,menu;
    //end rem



    Thread t;
    boolean b=true;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        mAuth=FirebaseAuth.getInstance();

        updateUI();
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()&&b) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
            }
        };
    }

    void setupUI(){
        displayName=(TextView)findViewById(R.id.mndisplayname);
        weight=(TextView)findViewById(R.id.mnweight);
        callorie=(TextView)findViewById(R.id.mncallorie);
        stepCount=(TextView)findViewById(R.id.mnstepcount);
        height=(TextView)findViewById(R.id.mnheight);
        mSignOut=(Button)findViewById(R.id.mnsignout);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        stat=(Button)findViewById(R.id.mnstat);
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MyStatsActivity.class));
            }
        });
        menu=(Button)findViewById(R.id.mnmenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MainMenuActivity.class));
            }
        });



    }

    void updateUI(){

        SharedPreferences pref = MainActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
        displayName.setText(pref.getString(SharedPrefData.DISPLAYNAME,""));
        weight.setText("weight: "+pref.getInt(SharedPrefData.WEIGHT,0));
        SharedPreferences calorieMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        callorie.setText("callorie:"+calorieMapPref.getInt(new Day().toString(),0));
        SharedPreferences stepCountMapPref = MainActivity.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        stepCount.setText("Stepcount: "+stepCountMapPref.getInt(new Day().toString(),0));
        height.setText("height: "+pref.getInt(SharedPrefData.HEIGHT,0));


    }

    void signOut(){
        b=false;
        SharedPrefData sharedPrefData=new SharedPrefData(MainActivity.this);
        sharedPrefData.clear();
        Intent intent=new Intent(MainActivity.this,FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(MainActivity.this,StepDetector.class);
        stopService(intent2);
        mAuth.signOut();
    }

 

    @Override
    protected void onDestroy() {
        super.onDestroy();
        b=false;
    }
}
