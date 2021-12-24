package com.google.mlkit.vision.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.mlkit.vision.demo.java.LivePreviewActivity;

public class LiveFragment extends Fragment {


    public LiveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent=new Intent(getActivity(), LivePreviewActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_live, container, false);
    }
}