package com.blogspot.athletio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import general.SocialPost;
import services.FirebaseUploadService;
import stepdetector.StepDetector;
import storage.SharedPrefData;
import utility.Constants;
import utility.FirebaseUtils;

public class SocialMainActivity extends AppCompatActivity {
    Button createPostButton, searchButton, messengerButton;
    private FirebaseRecyclerAdapter<SocialPost, PostHolder> mPostAdapter;
    private RecyclerView mPostRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_main);
        init();
        createPostButton = (Button) findViewById(R.id.social_main_new_post);
        searchButton = (Button) findViewById(R.id.social_main_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialMainActivity.this, ShowUserListActivity.class));
            }
        });

        messengerButton = (Button) findViewById(R.id.social_main_msg_button);
        messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialMainActivity.this, OnlineChatLogin.class));
            }
        });
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialMainActivity.this, SocialPostCreateActivity.class));
            }
        });
    }

    private void init() {
        mPostRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_post);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter();
        mPostRecyclerView.setAdapter(mPostAdapter);
    }

    private void setupAdapter() {
        mPostAdapter = new FirebaseRecyclerAdapter<SocialPost, PostHolder>(
                SocialPost.class,
                R.layout.row_post,
                SocialMainActivity.PostHolder.class,
                FirebaseUtils.getPostRef()
        ) {
            protected void populateViewHolder(PostHolder viewHolder, final SocialPost model, int position) {
                viewHolder.setNumCOmments(String.valueOf(model.getNumComments()));
                viewHolder.setNumLikes(String.valueOf(model.getNumLikes()));
                viewHolder.setTIme(DateUtils.getRelativeTimeSpanString(model.getTimeCreated()));
                viewHolder.setUsername(model.getUser().getUser());
                viewHolder.setPostText(model.getPostText());

                Glide.with(SocialMainActivity.this)
                        .load(model.getUser().getPhotoUrl())
                        .into(viewHolder.postOwnerDisplayImageView);

                if (model.getPostImageUrl() != null) {
                    viewHolder.postDisplayImageVIew.setVisibility(View.VISIBLE);
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference(model.getPostImageUrl());
                    Glide.with(SocialMainActivity.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(viewHolder.postDisplayImageVIew);
                } else {
                    viewHolder.postDisplayImageVIew.setImageBitmap(null);
                    viewHolder.postDisplayImageVIew.setVisibility(View.GONE);
                }
                viewHolder.postLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClick(model.getPostId());
                    }
                });

                viewHolder.postCommentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SocialMainActivity.this, SocialPostActivity.class);
                        intent.putExtra(Constants.EXTRA_POST, model);
                        startActivity(intent);
                    }
                });
            }
        };
    }

    private void onLikeClick(final String postId) {
        FirebaseUtils.getPostLikedRef(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            //SocialUser liked
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num - 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FirebaseUtils.getPostRef()
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num + 1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            FirebaseUtils.getPostLikedRef(postId)
                                                    .setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                startActivity(new Intent(this, MainActivity.class));
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

    void signOut() {
        SharedPrefData sharedPrefData = new SharedPrefData(this);
        sharedPrefData.clear();
        Intent intent = new Intent(this, FirebaseUploadService.class);
        stopService(intent);

        Intent intent2 = new Intent(this, StepDetector.class);
        stopService(intent2);
        FirebaseAuth.getInstance().signOut();
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
        public ImageView postOwnerDisplayImageView;
        public TextView postOwnerUsernameTextView;
        public TextView postTimeCreatedTextView;
        public ImageView postDisplayImageVIew;
        public TextView postTextTextView;
        LinearLayout postLikeLayout;
        LinearLayout postCommentLayout;
        TextView postNumLikesTextView;
        TextView postNumCommentsTextView;


        public PostHolder(View itemView) {
            super(itemView);
            postOwnerDisplayImageView = (ImageView) itemView.findViewById(R.id.acitvity_social_post_owner_display);
            postOwnerUsernameTextView = (TextView) itemView.findViewById(R.id.acitvity_social_post_username);
            postTimeCreatedTextView = (TextView) itemView.findViewById(R.id.acitvity_social_time);
            postDisplayImageVIew = (ImageView) itemView.findViewById(R.id.acitvity_social_post_display);
            postLikeLayout = (LinearLayout) itemView.findViewById(R.id.acitvity_social_like_layout);
            postCommentLayout = (LinearLayout) itemView.findViewById(R.id.acitvity_social_comment_layout);
            postNumLikesTextView = (TextView) itemView.findViewById(R.id.acitvity_social_likes);
            postNumCommentsTextView = (TextView) itemView.findViewById(R.id.acitvity_social_comments);
            postTextTextView = (TextView) itemView.findViewById(R.id.acitvity_social_post_text);
        }

        public void setUsername(String username) {
            postOwnerUsernameTextView.setText(username);
        }

        public void setTIme(CharSequence time) {
            postTimeCreatedTextView.setText(time);
        }

        public void setNumLikes(String numLikes) {
            postNumLikesTextView.setText(numLikes);
        }

        public void setNumCOmments(String numComments) {
            postNumCommentsTextView.setText(numComments);
        }

        public void setPostText(String text) {
            postTextTextView.setText(text);
        }

    }
}

