package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Vector;

import adapters.SmallUserCardAdapter;
import general.SmallUser;

public class ShowUserListActivity extends AppCompatActivity {
    DatabaseReference mDatabase;

    List<SmallUser> smallUsers;
    RecyclerView recList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_list);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Userlist");
        smallUsers=new Vector<SmallUser>();
        recList = (RecyclerView) findViewById(R.id.smallusercardList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        setupUI();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                       SmallUser smallUser=new SmallUser(d.getValue().toString());
                        smallUsers.add(smallUser);
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
        recList.setAdapter(new SmallUserCardAdapter(smallUsers));
    }

    private void setupUI() {

    }

    private void showProfile(String uid) {
        Intent intent=new Intent(ShowUserListActivity.this,ShowProfileActivity.class);
        intent.putExtra("UID",uid);
        startActivity(intent);
    }
}
