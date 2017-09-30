package com.blogspot.athletio;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.net.Inet4Address;

public class SettingsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;


    Button deletealldatabt,submit;
    EditText birthDate,birthMonth,birthYear,gender,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();


        setupUI();
    }

    private void setupUI() {
        deletealldatabt=(Button)findViewById(R.id.settingsdeletealldatabt);
        deletealldatabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=SettingsActivity.this;
                SharedPreferences weightMapPref = context.getSharedPreferences(SharedPrefData.WEIGHTMAP, MODE_PRIVATE);
                SharedPreferences.Editor weightMapEditor = weightMapPref.edit();
                weightMapEditor.clear();
                weightMapEditor.commit();


                SharedPreferences stepCountMapPref = context.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
                SharedPreferences.Editor stepCountMapeditor =  stepCountMapPref.edit();
                stepCountMapeditor.clear();
                stepCountMapeditor.commit();

                SharedPreferences calorieMapPref = context.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
                SharedPreferences.Editor calorieMapeditor =  calorieMapPref.edit();
                calorieMapeditor.clear();
                calorieMapeditor.commit();



            }
        });
        birthDate=(EditText)findViewById(R.id.settingsbirthdate);
        birthMonth=(EditText)findViewById(R.id.settingsbirthmonth);
        birthYear=(EditText)findViewById(R.id.settingsbirthyear);
        gender=(EditText)findViewById(R.id.settingsgender);
        height=(EditText)findViewById(R.id.settingsheight);
        submit=(Button)findViewById(R.id.settingssubmitbutton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo=new UserInfo(mAuth.getCurrentUser().getDisplayName(),Integer.parseInt(birthDate.getText().toString()),Integer.parseInt(birthMonth.getText().toString()),Integer.parseInt(birthYear.getText().toString()),gender.getText().toString(),mAuth.getCurrentUser().getEmail());

                SharedPreferences pref = SettingsActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(SharedPrefData.DISPLAYNAME,mAuth.getCurrentUser().getDisplayName());
                editor.putInt(SharedPrefData.BIRTHDATE,Integer.parseInt(birthDate.getText().toString()));
                editor.putInt(SharedPrefData.BIRTHMONTH,Integer.parseInt(birthMonth.getText().toString()));
                editor.putInt(SharedPrefData.BIRTHYEAR,Integer.parseInt(birthYear.getText().toString()));
                editor.putString(SharedPrefData.GENDER,gender.getText().toString());
                editor.putString(SharedPrefData.EMAIL,mAuth.getCurrentUser().getEmail());
                editor.putInt(SharedPrefData.HEIGHT,Integer.parseInt(height.getText().toString()));
                editor.commit();
                Toast.makeText(SettingsActivity.this,"Updated Successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}
