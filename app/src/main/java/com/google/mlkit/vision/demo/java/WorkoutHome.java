package com.google.mlkit.vision.demo.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.posedetector.Workout;

import java.util.ArrayList;

public class WorkoutHome extends AppCompatActivity {
    private WorkoutAdapter adapter;
    private ArrayList<Workout> workoutList;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private RecyclerView recyclerView;
    private WorkoutAdapter.RecyclerViewCLickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouthistory);
        recyclerView= findViewById(R.id.rv_history);
        workoutList = new ArrayList<>();
        getSupportActionBar().hide();
        setWorkoutInfo();
        setAdapter();
    }

    private void setWorkoutInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Workouts history3");
        userID=user.getUid();

        FirebaseDatabase.getInstance().getReference().child("Workouts history3")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Workout> workouts=new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Workout workout = snapshot.getValue(Workout.class);
                            if (workout.getuID().equals(userID)){
                                workouts.add(workout);
                            }
                        }
                        workoutList.clear();
                        workoutList.addAll(workouts);
                        adapter.setList(workoutList);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(WorkoutHome.this,"Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void setAdapter(){
        setonClickListener();
        adapter= new WorkoutAdapter(workoutList,listener);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration divider=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(divider);

        recyclerView.setAdapter(adapter);

    }

    private void setonClickListener() {
        listener=new WorkoutAdapter.RecyclerViewCLickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(getApplicationContext(),DetailedWorkoutHistory.class);
                intent.putExtra("workoutName",workoutList.get(position).getExerciseName());
                intent.putExtra("Date",workoutList.get(position).getDate());
                intent.putExtra("Sets",String.valueOf(workoutList.get(position).getSets()));
                intent.putExtra("Repetitions",String.valueOf(workoutList.get(position).getRepititions()));
                intent.putExtra("Feedback",workoutList.get(position).getFeedback());
                startActivity(intent);
            }
        };
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.setList(workoutList);
        recyclerView.getAdapter().notifyDataSetChanged();

    }
}
