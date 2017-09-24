package com.blogspot.athletio;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdditionalInfoInputActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    User user;
    SharedPrefData sharedPrefData;

    //UI variables - START
    EditText birthDate,birthMonth,birthYear,gender,height,weight;
    Button mFirebaseButton;
    //UI variables - END

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info_input);

        //rem
        birthDate=(EditText)findViewById(R.id.birthdate);
        birthMonth=(EditText)findViewById(R.id.birthmonth);
        birthYear=(EditText)findViewById(R.id.birthyear);
        gender=(EditText)findViewById(R.id.gender);
        mFirebaseButton=(Button)findViewById(R.id.button);
        height=(EditText)findViewById(R.id.height);
        weight=(EditText)findViewById(R.id.weight);
        height.setVisibility(View.INVISIBLE);
        weight.setVisibility(View.INVISIBLE);
        birthDate.setVisibility(View.INVISIBLE);
        birthMonth.setVisibility(View.INVISIBLE);
        birthYear.setVisibility(View.INVISIBLE);
        gender.setVisibility(View.INVISIBLE);
        mFirebaseButton.setVisibility(View.INVISIBLE);
        //end rem

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        sharedPrefData=new SharedPrefData(this);


        if(sharedPrefData.getSaved()==true){

            startActivity(new Intent(AdditionalInfoInputActivity.this,MainActivity.class));
            finish();
        }
        else{
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        Log.d("Firebase get ",dataSnapshot.getValue().toString());
                        User user=new User(dataSnapshot.getValue().toString());
                        sharedPrefData.saveUser(user);


                        startService(new Intent(AdditionalInfoInputActivity.this,FirebaseUploadService.class));
                        startService(new Intent(AdditionalInfoInputActivity.this,StepDetectorService.class));

                        startActivity(new Intent(AdditionalInfoInputActivity.this,MainActivity.class));
                        finish();
                    }
                    else{
                        loadForm();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        mFirebaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check inputs

                user=new User(mAuth.getCurrentUser().getDisplayName(),Integer.parseInt(birthDate.getText().toString()),Integer.parseInt(birthMonth.getText().toString()),Integer.parseInt(birthYear.getText().toString()),gender.getText().toString(),mAuth.getCurrentUser().getEmail(),Integer.parseInt(height.getText().toString()),Integer.parseInt(weight.getText().toString()));
                mDatabase.setValue(user);
                sharedPrefData.saveUser(user);

                startService(new Intent(AdditionalInfoInputActivity.this,FirebaseUploadService.class));
                startService(new Intent(AdditionalInfoInputActivity.this,StepDetectorService.class));

                startActivity(new Intent(AdditionalInfoInputActivity.this,MainActivity.class));
                finish();

            }
        });
    }

    void loadForm(){
        height.setVisibility(View.VISIBLE);
        weight.setVisibility(View.VISIBLE);
        birthDate.setVisibility(View.VISIBLE);
        birthMonth.setVisibility(View.VISIBLE);
        birthYear.setVisibility(View.VISIBLE);
        gender.setVisibility(View.VISIBLE);
        mFirebaseButton.setVisibility(View.VISIBLE);
    }
}
