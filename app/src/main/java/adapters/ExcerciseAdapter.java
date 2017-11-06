package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.blogspot.athletio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import general.Exercise;
import general.RankPersons;


public class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ExerciseViewHolder> implements Filterable {

    public  List<Exercise> list;
    public List<Exercise> actualExerciseList;

    public ExcerciseAdapter(List<Exercise> list) {
        this.actualExerciseList = list;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_excercise, viewGroup, false);

        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise=list.get(position);
        holder.title.setText(exercise.title);
        holder.description.setText(exercise.description);
        holder.category.setText("Category: "+ exercise.category);
    }
    private android.widget.Filter fRecords;
    @Override
    public android.widget.Filter getFilter() {
        if(fRecords == null) {
            fRecords=new RecordFilter();
        }
        return fRecords;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView description;
        protected TextView category;

        public ExerciseViewHolder(View v) {
            super(v);
            title=(TextView)v.findViewById(R.id.excerciseCardTitle);
            description=(TextView)v.findViewById(R.id.excerciseCardDescrip);
            category=(TextView)v.findViewById(R.id.excerciseCardCategory);
        }
    }
    private class RecordFilter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = actualExerciseList;
                results.count = actualExerciseList.size();

            } else {
                ArrayList<Exercise> fRecords = new ArrayList<Exercise>();

                for (Exercise s : actualExerciseList) {
                    if (s.getTitle().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                        fRecords.add(s);
                    }
                    if(s.getCategory().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())){
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
            list = (ArrayList<Exercise>) results.values;
            notifyDataSetChanged();
        }

        public boolean isLoggable(LogRecord record) {
            return false;
        }

    }
}