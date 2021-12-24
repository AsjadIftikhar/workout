package com.google.mlkit.vision.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.mlkit.vision.demo.R;

public class edit_profile extends AppCompatActivity {
    Button prof_update;
    EditText prof_name, prof_email, prof_age, prof_height, prof_pass, prof_new_pass;
    TextView prof_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prof_name = findViewById(R.id.prof_name);
        prof_email = findViewById(R.id.prof_email);
        prof_age = findViewById(R.id.prof_age);
        prof_height = findViewById(R.id.prof_height);
        prof_pass = findViewById(R.id.prof_pass);
        prof_new_pass = findViewById(R.id.prof_new_pass);

        prof_status = findViewById(R.id.prof_status);
        prof_update = findViewById(R.id.prof_update);


    }

}