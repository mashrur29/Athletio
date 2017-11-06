package com.blogspot.athletio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import general.UserDetails;

/*
    From this Activity User can Login for an Online Chat account

    Reference: https://firebase.google.com/docs/database/admin/retrieve-data

 */

public class OnlineChatLogin extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UserDetails.username != "-1") {
            startActivity(new Intent(OnlineChatLogin.this, Users.class));
            finish();
        }

        setContentView(R.layout.activity_online_chat_login);

        registerUser = (TextView) findViewById(R.id.online_chat_register);
        username = (EditText) findViewById(R.id.online_chat_username);
        password = (EditText) findViewById(R.id.online_chat_password);
        loginButton = (Button) findViewById(R.id.online_chat_loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineChatLogin.this, OnlineChatRegister.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else {
                    String url = "https://doctor-d6368.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(OnlineChatLogin.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(OnlineChatLogin.this, "userName not found", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(OnlineChatLogin.this, "user not found", Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        startActivity(new Intent(OnlineChatLogin.this, Users.class));
                                        finish();
                                    } else {
                                        Toast.makeText(OnlineChatLogin.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(OnlineChatLogin.this);
                    rQueue.add(request);
                }

            }
        });
    }
}

