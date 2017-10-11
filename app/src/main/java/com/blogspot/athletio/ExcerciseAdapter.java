package com.blogspot.athletio;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

/**
 * Created by tanvir on 10/11/17.
 */

public class ExcerciseAdapter extends RecyclerView.Adapter<ExcerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> List;

    public ExcerciseAdapter(List<Exercise> List) {
        this.List = List;
    }

    @Override
    public int getItemCount() {
        return List.size();
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
        Exercise exercise=List.get(position);
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
