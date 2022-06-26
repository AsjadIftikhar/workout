package com.google.mlkit.vision.demo.java;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.demo.R;

public class DetailedWorkoutHistory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_workouthistory);
        getSupportActionBar().hide();
        TextView tv_workoutName=findViewById(R.id.tv_workout_name_value);
        TextView tv_date=findViewById(R.id.tv_date_value);
        TextView tv_sets=findViewById(R.id.tv_set_value);
        TextView tv_repetitions=findViewById(R.id.tv_rep_value);
        TextView tv_feedback=findViewById(R.id.tv_feedback_val);
        String workoutName="None";
        String date="None";
        String sets="None";
        String reps="None";
        String feedback="None";
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            workoutName=extras.getString("workoutName");
            date=extras.getString("Date");
            sets= extras.getString("Sets");
            reps=extras.getString("Repetitions");
            feedback=extras.getString("Feedback");
        }

        tv_workoutName.setText(workoutName);
        tv_date.setText(date);
        tv_sets.setText(sets);
        tv_repetitions.setText(reps);
        tv_feedback.setText(feedback);

    }

}
