package com.google.mlkit.vision.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.demo.java.BMI_calculation;
import com.google.mlkit.vision.demo.java.HomeActivity;
import com.google.mlkit.vision.demo.java.edit_profile;


public class ProfileFragment extends Fragment {

    Button sign_out;
    Button profile_page;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        sign_out=view.findViewById(R.id.btn_signout);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        profile_page=view.findViewById(R.id.btn_profile_activity);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), edit_profile.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}