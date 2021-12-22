package com.example.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,email,password,age,height,weight;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=findViewById(R.id.tv_name);
        email=findViewById(R.id.tv_email);
        password=findViewById(R.id.tv_password);
        age=findViewById(R.id.tv_Age);
        height=findViewById(R.id.tv_height);
        weight=findViewById(R.id.tv_weight);
        register_button=findViewById(R.id.btn_register);
    }
}