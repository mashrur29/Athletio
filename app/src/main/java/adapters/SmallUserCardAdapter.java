package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.athletio.R;
import com.blogspot.athletio.ShowProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import general.SmallUser;

/**
 * Created by tanvir on 10/12/17.
 */

public class SmallUserCardAdapter  extends RecyclerView.Adapter<SmallUserCardAdapter.SmallUserHolder>{
    private List<SmallUser> List;
    public Context context;

    public SmallUserCardAdapter(List<SmallUser> List) {
        this.List = List;
        SmallUserHolder.list=List;
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    @Override
    public void onBindViewHolder(SmallUserHolder holder, int i) {
        SmallUser smallUser = List.get(i);
        Picasso.with(context).load(smallUser.photo).into(holder.imageView);
        holder.textView.setText(smallUser.name);

    }

    @Override
    public SmallUserHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.showuserlist_card, viewGroup, false);
        context=viewGroup.getContext();
        return new SmallUserHolder(itemView);
    }


    public static class SmallUserHolder extends RecyclerView.ViewHolder {
        static List<SmallUser> list;
        protected ImageView imageView;
        protected  TextView textView;
        public SmallUserHolder(View v) {
            super(v);
            textView =  (TextView) v.findViewById(R.id.showuserlist_cardtv);
            imageView=(ImageView) v.findViewById(R.id.showuserlist_cardiv);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent=new Intent(v.getContext(),ShowProfileActivity.class);
                        intent.putExtra("UID",list.get(pos).UID.toString());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
