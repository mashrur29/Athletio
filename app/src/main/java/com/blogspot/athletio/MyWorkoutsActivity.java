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
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class MyWorkoutsActivity extends AppCompatActivity {
    DatabaseReference mDatabase,mWorkoutkeyDatabase;
    FirebaseAuth mAuth;
    Vector<String> workoutKeys=new Vector<String>();
    Vector<Workout> workouts=new Vector<Workout>();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workouts);
        mAuth=FirebaseAuth.getInstance();
        mWorkoutkeyDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("userData").child("workouts");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("workouts");

        setupUI();


        mWorkoutkeyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    workoutKeys.add(d.getValue().toString());
                }
                for (final String key:workoutKeys){
                    mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshotw) {

                            if(dataSnapshotw.getValue()!=null)
                            {
                                Workout workout=new Workout(dataSnapshotw.getValue().toString());
                                workout.key=key;
                                workouts.add(workout);
                                updateUI();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }


    void setupUI(){

        tv=(TextView)findViewById(R.id.myworkoutstv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workouts.get(0)!=null)
                {
                    showWorkout(workouts.get(0).key);
                }
            }
        });
    }

    void updateUI(){

        tv.setText(workouts.toString());

    }

    void showWorkout(String key){
        Intent intent=new Intent(MyWorkoutsActivity.this,ShowWorkoutActivity.class);
        intent.putExtra("WorkoutKey",key);
        startActivity(intent);
    }

}
