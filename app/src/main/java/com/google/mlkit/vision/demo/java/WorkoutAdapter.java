package com.google.mlkit.vision.demo.java;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.posedetector.Workout;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;


public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder> {

    private ArrayList<Workout> workoutList;
    Context context;
    public WorkoutAdapter(ArrayList<Workout> workoutList){
        this.workoutList= workoutList;
    }
    public void setList(ArrayList<Workout> workoutList)
    {
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listworkout, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.exerciseName.setText(workoutList.get(position).getExerciseName());
        holder.repCount.setText(String.valueOf(workoutList.get(position).getRepititions()));
        holder.date.setText(workoutList.get(position).getDate());
        holder.workout_image.setImageResource(R.drawable.ic_bicep);
        holder.iv_name.setImageResource(R.drawable.ic_name);
        holder.iv_reps.setImageResource(R.drawable.ic_repetition);
        holder.iv_date.setImageResource(R.drawable.ic_date);
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView exerciseName;
        private TextView repCount;
        private TextView date;
        private ImageView workout_image;
        private ImageView iv_name;
        private ImageView iv_reps;
        private ImageView iv_date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName= itemView.findViewById(R.id.workoutName);
            repCount= itemView.findViewById(R.id.repitition);
            date= itemView.findViewById(R.id.date);
            workout_image=itemView.findViewById(R.id.workout_image);
            iv_name=itemView.findViewById(R.id.iv_name);
            iv_reps=itemView.findViewById(R.id.iv_reps);
            iv_date=itemView.findViewById(R.id.iv_date);




        }
    }
}
