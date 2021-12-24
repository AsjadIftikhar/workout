package com.google.mlkit.vision.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.mlkit.vision.demo.java.BMI_calculation;
import com.google.mlkit.vision.demo.java.bodyfat_calculation;

public class HomeFragment extends Fragment {

    Button bmi_activity_button;
    Button bf_activity_button;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        bmi_activity_button=view.findViewById(R.id.btn_bmi_activity);
        bf_activity_button=view.findViewById(R.id.btn_bf_activity);
        bmi_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BMI_calculation.class);
                startActivity(intent);
            }
        });

        bf_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), bodyfat_calculation.class);
                startActivity(intent);
            }
        });
        return  view;
    }
}