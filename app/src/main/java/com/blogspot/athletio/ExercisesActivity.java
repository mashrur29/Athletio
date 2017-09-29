package com.blogspot.athletio;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ExercisesActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    Vector<Exercise> exercises;

    TextView tv;
    EditText searchet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Exersices");

        setupUI();

        exercises=new Vector<Exercise>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        exercises.add(new Exercise(d.getValue().toString()));
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
        tv.setText(exercises.toString());
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.exercisestv);
        searchet=(EditText)findViewById(R.id.exercisessearchet);
        searchet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //reorder vector according to search and call updateUI
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
