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

    private RecyclerViewCLickListener listener;
    private ArrayList<Workout> workoutList;
    public WorkoutAdapter(ArrayList<Workout> workoutList,RecyclerViewCLickListener listener){
        this.workoutList= workoutList;
        this.listener=listener;
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
        holder.date.setText(workoutList.get(position).getDate());
        holder.workout_image.setImageResource(R.drawable.ic_bicep);
        holder.iv_name.setImageResource(R.drawable.ic_name);
        holder.iv_date.setImageResource(R.drawable.ic_date);
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView exerciseName;
        private TextView date;
        private ImageView workout_image;
        private ImageView iv_name;
        private ImageView iv_date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName= itemView.findViewById(R.id.workoutName);
            date= itemView.findViewById(R.id.date);
            workout_image=itemView.findViewById(R.id.workout_image);
            iv_name=itemView.findViewById(R.id.iv_name);
            iv_date=itemView.findViewById(R.id.iv_date);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    public interface RecyclerViewCLickListener{
        void onClick(View v,int position);
    }
}
