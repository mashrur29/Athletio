package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import general.SmallUser;
import general.User;
import services.FirebaseUploadService;
import services.StepDetectorService;
import storage.SharedPrefData;

public class AdditionalInfoInputActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    User user;
    SharedPrefData sharedPrefData;



    int year, month,day;
    String gender;

    EditText height,weight;
    Button mFirebaseButton;
    Spinner yearSpinner,monthSpinner,daySpinner,genderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_info_input);

        yearSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_year_spinner);
        monthSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_month_spinner);
        daySpinner = (Spinner) findViewById(R.id.additional_info_input_layout_day_spinner);
        genderSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_gender_spinner);
        height=(EditText)findViewById(R.id.additional_info_input_layout_height_edittext);
        weight=(EditText)findViewById(R.id.additional_info_input_layout_weight_edittext);
        addItemOnYearSpinner();
        addItemOnDaySpinner();
        addItemOnMonthSpinner();
        addItemOnGenderSpinner();
        yearSpinner.setOnItemSelectedListener(this);
        monthSpinner.setOnItemSelectedListener(this);
        daySpinner.setOnItemSelectedListener(this);
        genderSpinner.setOnItemSelectedListener(this);
        mFirebaseButton=(Button)findViewById(R.id.additional_info_input_layout_submit_button);


        final RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.additional_info_input_layout);
        relativeLayout.setVisibility(View.INVISIBLE);


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


                        startService(new Intent(AdditionalInfoInputActivity.this, FirebaseUploadService.class));
                        startService(new Intent(AdditionalInfoInputActivity.this, StepDetectorService.class));

                        startActivity(new Intent(AdditionalInfoInputActivity.this,MainActivity.class));
                        finish();

                    }
                    else{
                        relativeLayout.setVisibility(View.VISIBLE);
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

                try{
                    Integer.parseInt(height.getText().toString());
                }catch (Exception e){
                    Toast.makeText(AdditionalInfoInputActivity.this,"Height is Wrong",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Integer.parseInt(weight.getText().toString());
                }catch (Exception e){
                    Toast.makeText(AdditionalInfoInputActivity.this,"Weight is Wrong",Toast.LENGTH_SHORT).show();
                    return;
                }

                user=new User(mAuth.getCurrentUser().getDisplayName(),day, month, year, gender,mAuth.getCurrentUser().getEmail(),Integer.parseInt(height.getText().toString()),Integer.parseInt(weight.getText().toString()));
                mDatabase.setValue(user);
                FirebaseDatabase.getInstance().getReference().child("Userlist").child(mAuth.getCurrentUser().getUid()).setValue(new SmallUser(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),mAuth.getCurrentUser().getPhotoUrl().toString()));

                sharedPrefData.saveUser(user);

                startService(new Intent(AdditionalInfoInputActivity.this,FirebaseUploadService.class));
                startService(new Intent(AdditionalInfoInputActivity.this,StepDetectorService.class));

                startActivity(new Intent(AdditionalInfoInputActivity.this,MainActivity.class));
                finish();

            }
        });
    }


    public void addItemOnMonthSpinner()
    {
        List<Integer> year = new ArrayList<Integer>();
        for(int i = 1; i <= 12; i++ )
        {
            year.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, year);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner yearSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_month_spinner);
        yearSpinner.setAdapter(dataAdapter);
        return;
    }
    public void addItemOnDaySpinner()
    {
        List<Integer> day = new ArrayList<Integer>();
        for(int i = 1; i <= 31; i++ )
        {
            day.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, day);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner daySpinner = (Spinner) findViewById(R.id.additional_info_input_layout_day_spinner);
        daySpinner.setAdapter(dataAdapter);
        return;
    }
    public void addItemOnYearSpinner()
    {
        List<Integer> month = new ArrayList<Integer>();
        for(int i = 1950; i <= 2150; i++ )
        {
            month.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner monthSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_year_spinner);
        monthSpinner.setAdapter(dataAdapter);
        return;
    }
    public void addItemOnGenderSpinner()
    {
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("FeMale");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner genderSpinner = (Spinner) findViewById(R.id.additional_info_input_layout_gender_spinner);
        genderSpinner.setAdapter(dataAdapter);
        return;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.additional_info_input_layout_day_spinner)
        {
            day=Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.additional_info_input_layout_month_spinner)
        {
            month =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.additional_info_input_layout_year_spinner)
        {
            year =Integer.parseInt(item);
        }
        else if(spinner.getId() == R.id.additional_info_input_layout_gender_spinner)
        {
            gender =item;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}
