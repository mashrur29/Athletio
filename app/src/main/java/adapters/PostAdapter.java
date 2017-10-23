package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.athletio.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import general.Post;

/**
 * Created by tanvir on 10/12/17.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post> list;
    public Context context;

    public PostAdapter(List<Post> list) {
        this.list=list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int i) {
        Post post=list.get(i);
        holder.time.setText(" "+post.day.day+"/"+post.day.month+"/"+post.day.year+"  "+String.format("%02d", post.hour)+" : "+String.format("%02d", post.min));
        holder.uName.setText(" "+post.uName);
        holder.body.setText(" "+post.body);
        if(post.type==1){
            Picasso.with(context).load(post.photoUri).into(holder.imageView);
        }
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_posts, viewGroup, false);
        context=viewGroup.getContext();
        return new PostViewHolder(itemView);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        protected TextView time;
        protected TextView uName;
        protected TextView body;
        protected ImageView imageView;

        public PostViewHolder(View v) {
            super(v);
            time =  (TextView) v.findViewById(R.id.postcardTimeDate);
            uName = (TextView)  v.findViewById(R.id.postcarduserName);
            body = (TextView)  v.findViewById(R.id.postcardDescription);
            imageView = (ImageView) v.findViewById(R.id.poscardImage);
        }
    }
}
