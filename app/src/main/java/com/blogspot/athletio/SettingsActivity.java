package com.blogspot.athletio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import general.User;
import general.UserInfo;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;


///Allows user to make changes of their birthday,height
public class SettingsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    Button submitButton;
    EditText birthDateEdittext, birthMonthEdittext, birthYearEdittext, heightEdittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth=FirebaseAuth.getInstance();


        setupUI();
    }

    private void setupUI() {
        User user=new SharedPrefData(SettingsActivity.this).getUser();
        birthDateEdittext =(EditText)findViewById(R.id.settings_layout_birthdate_edittext);
        birthDateEdittext.setText(user.userInfo.birthDate+"");
        birthMonthEdittext =(EditText)findViewById(R.id.settings_layout_birthmonth_edittext);
        birthMonthEdittext.setText(user.userInfo.birthMonth+"");
        birthYearEdittext =(EditText)findViewById(R.id.settings_layout_birthyear_edittext);
        birthYearEdittext.setText(user.userInfo.birthYear+"");
        heightEdittext =(EditText)findViewById(R.id.settings_layout_height_edittext);
        heightEdittext.setText(user.userData.height+"");
        submitButton =(Button)findViewById(R.id.settings_layout_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Integer.parseInt(birthDateEdittext.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(SettingsActivity.this,"Put Bithdate in correct form",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Integer.parseInt(birthMonthEdittext.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(SettingsActivity.this,"Put Bithmonth in correct form",Toast.LENGTH_SHORT).show();
                    return;

                }
                try{
                    Integer.parseInt(birthYearEdittext.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(SettingsActivity.this,"Put Birthyear in correct form",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Integer.parseInt(heightEdittext.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(SettingsActivity.this,"Put Height in correct form",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserInfo userInfo=new UserInfo(mAuth.getCurrentUser().getDisplayName(),Integer.parseInt(birthDateEdittext.getText().toString()),Integer.parseInt(birthMonthEdittext.getText().toString()),Integer.parseInt(birthYearEdittext.getText().toString()),new SharedPrefData(SettingsActivity.this).getUser().userInfo.gender,mAuth.getCurrentUser().getEmail());

                SharedPreferences pref = SettingsActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(SharedPrefData.DISPLAYNAME,mAuth.getCurrentUser().getDisplayName());
                editor.putInt(SharedPrefData.BIRTHDATE,Integer.parseInt(birthDateEdittext.getText().toString()));
                editor.putInt(SharedPrefData.BIRTHMONTH,Integer.parseInt(birthMonthEdittext.getText().toString()));
                editor.putInt(SharedPrefData.BIRTHYEAR,Integer.parseInt(birthYearEdittext.getText().toString()));
                editor.putString(SharedPrefData.EMAIL,mAuth.getCurrentUser().getEmail());
                editor.putInt(SharedPrefData.HEIGHT,Integer.parseInt(heightEdittext.getText().toString()));
                editor.commit();
                Toast.makeText(SettingsActivity.this,"Updated Successfully",Toast.LENGTH_LONG).show();
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                return true;
            case R.id.menu_track_workout:
                startActivity(new Intent(this, TrackWorkoutMenuActivity.class));
                finish();
                return true;
            case R.id.menu_online_workout:
                startActivity(new Intent(this, OnlineWorkoutActivity.class));
                finish();
                return true;
            case R.id.menu_my_workouts:
                startActivity(new Intent(this, MyWorkoutsActivity.class));
                finish();
                return true;
            case R.id.menu_excersices:
                startActivity(new Intent(this, ExercisesActivity.class));
                finish();
                return true;
            case R.id.menu_social:
                startActivity(new Intent(this, SocialMainActivity.class));
                finish();
                return true;
            case R.id.menu_events:
                startActivity(new Intent(this, EventsActivity.class));
                finish();
                return true;
            case R.id.menu_event_reminder:
                startActivity(new Intent(this, ShowEventRemindersActivity.class));
                finish();
                return true;
            case R.id.menu_create_event:
                startActivity(new Intent(this, CreateEventActivity.class));
                finish();
                return true;
            case R.id.menu_nearby_place:
                startActivity(new Intent(this, MapsActivity.class));
                finish();
                return true;
            case R.id.menu_chat_bot:
                startActivity(new Intent(this, ChatBotMain.class));
                finish();
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            case R.id.menu_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void signOut(){
        SharedPrefData sharedPrefData=new SharedPrefData(this);
        sharedPrefData.clear();
        Intent intent=new Intent(this, FirebaseUploadService.class);
        stopService(intent);

        Intent intent2=new Intent(this, StepDetector.class);
        stopService(intent2);
        FirebaseAuth.getInstance().signOut();
    }
}
