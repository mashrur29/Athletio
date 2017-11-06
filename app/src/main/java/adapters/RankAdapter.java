package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import com.blogspot.athletio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import general.RankPersons;

/**
 * Created by zero639 on 10/25/17.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> implements Filterable {

    private List<RankPersons> personsList , actualPersonsList;

    public RankAdapter(List<RankPersons> personsList) {
        this.personsList = personsList;
        this.actualPersonsList = personsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rankpersons, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RankPersons rankPersons = personsList.get(position);
        holder.name.setText(rankPersons.getName());
        holder.steps.setText(rankPersons.getSteps());
        holder.caloryBurnt.setText(rankPersons.getCaloryBurnt());
        holder.weight.setText(rankPersons.getWeight());
    }

    @Override
    public int getItemCount() {
        return personsList.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, steps, weight, caloryBurnt;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.rank_person_name_textview);
            steps = (TextView) view.findViewById(R.id.rank_person_step_count_textview);
            weight = (TextView) view.findViewById(R.id.rank_person_weight_textview);
            caloryBurnt = (TextView) view.findViewById(R.id.rank_person_calory_burnt_textview);
        }
    }
    private class RecordFilter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = actualPersonsList;
                results.count = actualPersonsList.size();

            } else {
                ArrayList<RankPersons> fRecords = new ArrayList<RankPersons>();

                for (RankPersons s : actualPersonsList) {
                    if (s.getName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
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
            personsList = (ArrayList<RankPersons>) results.values;
            notifyDataSetChanged();
        }

        public boolean isLoggable(LogRecord record) {
            return false;
        }

    }
}