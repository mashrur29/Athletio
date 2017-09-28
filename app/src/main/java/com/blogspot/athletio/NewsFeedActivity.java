package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class NewsFeedActivity extends AppCompatActivity {
    DatabaseReference mDatabase;

    Vector<Post> posts=new Vector<Post>();


    TextView tv;
    Button searchpbt,postbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Post post=new Post(d.getValue().toString());
                    posts.add(post);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setupUI();

    }

    private void updateUI() {
        tv.setText(posts.toString());
    }

    private void setupUI() {
        searchpbt=(Button)findViewById(R.id.newsfeedsearchpersonbt);
        searchpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this,ShowUserListActivity.class));
            }
        });
        postbt=(Button)findViewById(R.id.newsfeedpostbt);
        postbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this,PostPublishActivity.class));
            }
        });
        tv=(TextView)findViewById(R.id.newsfeedtv);
    }
}
