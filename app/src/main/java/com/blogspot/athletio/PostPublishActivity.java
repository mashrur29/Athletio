package com.blogspot.athletio;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import general.Day;
import general.Post;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;

public class PostPublishActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    StorageReference storageReference;
    public static final int GALLERY_INTENT=2;
    String photoUri,postId;
    ProgressDialog progressDialog;
    int type= Post.TEXT;


    EditText postBody;
    ImageView postImageView;
    Button postSubmitButton, uploadPhotoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_publish);


        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Posts");
        storageReference= FirebaseStorage.getInstance().getReference().child("Photos");
        progressDialog=new ProgressDialog(this);

        setupUI();
    }

    private void setupUI() {
        postBody=(EditText)findViewById(R.id.post_publish_layout_post_body_edittext);
        postImageView =(ImageView)findViewById(R.id.post_publish_layout_post_imageview);
        postSubmitButton =(Button)findViewById(R.id.post_publish_layout_submit_button);
        postSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishPost(postBody.getText().toString());
            }
        });
        uploadPhotoButton =(Button)findViewById(R.id.post_publish_layout_upload_photo_button);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
    }

    private void publishPost(String body) {
        if(type==Post.PHOTO){
            Post post=new Post(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),postId,body,photoUri,Post.PHOTO,new Day(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
            mDatabase.child(postId).setValue(post);
        }
        else{
            postId=mDatabase.push().getKey();
            Post post=new Post(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),postId,body,Post.TEXT,new Day(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
            mDatabase.child(postId).setValue(post);
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Posts").child(postId).setValue(postId);
        Toast.makeText(PostPublishActivity.this,"Posted Successfully",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void choosePhoto() {
        postId=mDatabase.push().getKey();
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT&&resultCode==RESULT_OK){

            progressDialog.setMessage("UPLOADING...");
            progressDialog.show();

            Uri uri=data.getData();
            storageReference.child(postId).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostPublishActivity.this,"Upload Done",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    @SuppressWarnings("VisibleForTests")
                    Uri photoUrl=taskSnapshot.getDownloadUrl();
                    photoUri=photoUrl.toString();
                    type=Post.PHOTO;
                    Picasso.with(PostPublishActivity.this).load(photoUrl).into(postImageView);
                }
            });
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                startActivity(new Intent(this,MainActivity.class));
                return true;
            case R.id.menuTrackWorkout:
                startActivity(new Intent(this, TrackWorkoutMenuActivity.class));
                return true;
            case R.id.menuOnlineWorkout:
                startActivity(new Intent(this, OnlineWorkoutActivity.class));
                return true;
            case R.id.menuMyWorkouts:
                startActivity(new Intent(this, MyWorkoutsActivity.class));
                return true;
            case R.id.menuExcersices:
                startActivity(new Intent(this, ExercisesActivity.class));
                return true;
            case R.id.menuSocial:
                startActivity(new Intent(this, NewsFeedActivity.class));
                return true;
            case R.id.menuEvents:
                startActivity(new Intent(this, EventsActivity.class));
                return true;
            case R.id.menuEventReminder:
                startActivity(new Intent(this, ShowEventRemindersActivity.class));
                return true;
            case R.id.menuCreateEvent:
                startActivity(new Intent(this, CreateEventActivity.class));
                return true;
            case R.id.menuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menuSignOut:
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
