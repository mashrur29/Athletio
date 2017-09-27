package com.blogspot.athletio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowProfileActivity extends AppCompatActivity {
    String UID;

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        UID=getIntent().getStringExtra("UID");

        setupUI();
        updateUI();
    }

    private void updateUI() {
        tv.setText(UID);
    }

    private void setupUI() {
        tv=(TextView)findViewById(R.id.showprofiletv);
    }

}
