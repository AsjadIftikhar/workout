package com.example.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {
    EditText email,password;
    Button login_button,signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        email=findViewById(R.id.tv_person_email);
        password=findViewById(R.id.tv_password);
        login_button=findViewById(R.id.btn_login);
        signup_button=findViewById(R.id.btn_signup);

    }
}