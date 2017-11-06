package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.athletio.R;
import com.blogspot.athletio.ShowProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import general.SmallUser;

/**
 * Created by tanvir on 10/12/17.
 */

public class SmallUserCardAdapter  extends RecyclerView.Adapter<SmallUserCardAdapter.SmallUserHolder> implements Filterable {
    private List<SmallUser> list , actualList;
    public Context context;

    public SmallUserCardAdapter(List<SmallUser> list) {
        this.list = list;
        SmallUserHolder.list=list;
        actualList = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(SmallUserHolder holder, int i) {
        SmallUser smallUser = list.get(i);
        Picasso.with(context).load(smallUser.photo).into(holder.imageView);
        holder.textView.setText(smallUser.name);

    }

    @Override
    public SmallUserHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_showuserlist, viewGroup, false);
        context=viewGroup.getContext();
        return new SmallUserHolder(itemView);
    }
    private android.widget.Filter fRecords;

    //return the filter class object
    @Override
    public android.widget.Filter getFilter() {
        if(fRecords == null) {
            fRecords=new RecordFilter();
        }
        return fRecords;
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
                        intent.putExtra("PhotoId" ,list.get(pos).photo.toString());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
    //filter class
    private class RecordFilter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            //Implement filter logic
            // if edittext is null return the actual list
            if (constraint == null || constraint.length() == 0) {
                //No need for filter
                results.values = actualList;
                results.count = actualList.size();

            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list
                ArrayList<SmallUser> fRecords = new ArrayList<SmallUser>();

                for (SmallUser s : actualList) {
                    if (s.name.toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                        fRecords.add(s);
                    }
                }
                results.values = fRecords;
                results.count = fRecords.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {

            //it set the data from filter to adapter list and refresh the recyclerview adapter
            list = (ArrayList<SmallUser>) results.values;
            SmallUserHolder.list = (ArrayList<SmallUser>) results.values;
            notifyDataSetChanged();
        }

        public boolean isLoggable(LogRecord record) {
            return false;
        }

    }
}
