package com.blogspot.athletio;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import general.Day;
import general.User;
import storage.SharedPrefData;

public class MyStatsActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    User user;

    //rem
    TextView weighttv,steptv,calorietv;
    EditText weightet;
    Button updateweightbt;
    //end rem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stats);

        setupUI();

        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());



        updateUI();
    }

    private void updateUI() {
        SharedPreferences weightMapPref = MyStatsActivity.this.getSharedPreferences(SharedPrefData.WEIGHTMAP, MODE_PRIVATE);
        final SharedPreferences.Editor weightMapEditor = weightMapPref.edit();
        weighttv.setText("Weights"+weightMapPref.getInt(new Day().toString(),0));//put new Day(1,2,3) to get weight on 1/2/3
        SharedPreferences stepCountMapPref = MyStatsActivity.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        steptv.setText("steps"+stepCountMapPref.getInt(new Day().toString(),0));
        SharedPreferences calorieMapPref = MyStatsActivity.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        calorietv.setText("callories"+calorieMapPref.getInt(new Day().toString(),0));
        updateweightbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightMapEditor.putInt(new Day().toString(),Integer.parseInt(weightet.getText().toString()));
                weightMapEditor.commit();
                SharedPreferences pref = MyStatsActivity.this.getSharedPreferences(SharedPrefData.USERINFO, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(SharedPrefData.WEIGHT,Integer.parseInt(weightet.getText().toString()));
                editor.commit();
                updateUI();
            }
        });
    }

    void setupUI(){

        weighttv=(TextView)findViewById(R.id.mystatsweighttv);
        steptv=(TextView)findViewById(R.id.mystatssteptv);
        calorietv=(TextView)findViewById(R.id.mystatscalloriestv);
        weightet=(EditText)findViewById(R.id.mystatsweightet);
        updateweightbt=(Button)findViewById(R.id.mystatsupdateweightbt);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}
