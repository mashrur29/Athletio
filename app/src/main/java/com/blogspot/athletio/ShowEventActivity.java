package com.blogspot.athletio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowEventActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    Event event;

    TextView tv;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Events");

        String key=getIntent().getStringExtra("EVENT");
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
            @Override
            public void onClick(View view) {

            }
        });
    }

}
