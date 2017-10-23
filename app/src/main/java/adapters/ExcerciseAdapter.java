package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.athletio.R;

import java.util.List;

import general.Exercise;

/**
 * Created by tanvir on 10/11/17.
 */

public class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ExerciseViewHolder> {

    public  List<Exercise> list;

    public ExcerciseAdapter(List<Exercise> List) {
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
                inflate(R.layout.excercisecard, viewGroup, false);

        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise=list.get(position);
        holder.title.setText(exercise.title);
        holder.description.setText(exercise.description);
        holder.category.setText("Category: "+ exercise.category);
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
}
