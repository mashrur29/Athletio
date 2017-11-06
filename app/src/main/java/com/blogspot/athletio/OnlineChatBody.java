package com.blogspot.athletio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import general.UserDetails;

/*
    Reference: https://www.youtube.com/watch?v=Xn0tQHpMDnM
    Causes: Learning the use of firebase
 */

public class OnlineChatBody extends AppCompatActivity {
    LinearLayout userLayout;
    RelativeLayout friendLayout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase referenceUserName, referenceUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_chat_body);

        userLayout = (LinearLayout) findViewById(R.id.layout_mine);
        friendLayout = (RelativeLayout)findViewById(R.id.layout_friend);
        sendButton = (ImageView)findViewById(R.id.online_chat_send_button);
        messageArea = (EditText)findViewById(R.id.online_chat_messagearea_edittext);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        referenceUserName = new Firebase("https://doctor-d6368.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        referenceUserPassword = new Firebase("https://doctor-d6368.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("userName", UserDetails.username);
                    referenceUserName.push().setValue(map);
                    referenceUserPassword.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        referenceUserName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("userName").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You: \n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.chatWith + ": \n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(OnlineChatBody.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        userLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}