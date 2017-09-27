package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewsFeedActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        setupUI();

    }

    private void setupUI() {
        button=(Button)findViewById(R.id.newsfeedbt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this,ShowUserListActivity.class));
            }
        });
    }
}
