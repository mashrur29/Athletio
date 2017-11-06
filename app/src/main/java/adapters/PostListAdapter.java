package adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import storage.AppController;
import views.FeedImageView;

import com.blogspot.athletio.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import general.Post;


/**
 * Created by zero639 on 10/24/17.
 */

public class PostListAdapter extends BaseAdapter {
    TextView numberLikes;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> posts;
    private DatabaseReference mDatabase;

    public PostListAdapter(Activity activity, List<Post> posts) {
        this.activity = activity;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int location) {
        return posts.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.news_feed_username);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.news_feed_timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.news_feed_StatusMsg_textview);
        TextView url = (TextView) convertView.findViewById(R.id.news_feed_statusUrl_textview);
        NetworkImageView profilePic = (NetworkImageView) convertView
                .findViewById(R.id.news_feed_profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView
                .findViewById(R.id.news_feed_status_imageview);
        ImageView likeButton = (ImageView) convertView.findViewById(R.id.news_feed_like_imageview);
        numberLikes = (TextView) convertView.findViewById(R.id.news_feed_like_textview);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLike(posts.get(position).postId);
                Log.d("Shout", String.valueOf(posts.get(position).nLikes));
                posts.get(position).nLikes++;
            }
        });


        Post item = posts.get(position);
        numberLikes.setText(String.valueOf(item.nLikes));
        name.setText(item.uName);
        timestamp.setText(item.day.day + "/" + item.day.month + "/" + item.day.year);
        if (!TextUtils.isEmpty(item.body)) {
            statusMsg.setText(item.body);
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            statusMsg.setVisibility(View.GONE);
        }
        profilePic.setImageUrl(item.UDisplayPicURI, imageLoader);
        if (item.photoUri != null) {
            feedImageView.setImageUrl(item.photoUri, imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }
        return convertView;
    }

    void updateLike(final String postId) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {
                    Post post = new Post(dataSnapshot.getValue().toString());
                    post.nLikes++;
                    numberLikes.setText(String.valueOf(post.nLikes));
                    mDatabase.child("Posts").child(postId).setValue(post);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
