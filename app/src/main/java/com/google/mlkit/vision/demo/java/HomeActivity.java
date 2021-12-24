package com.google.mlkit.vision.demo.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.botton_nav;

public class HomeActivity extends AppCompatActivity {
    EditText email,password;
    Button login_button,signup_button;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        email=findViewById(R.id.tv_person_name);
        password=findViewById(R.id.tv_person_password);
        login_button=findViewById(R.id.btn_login);
        signup_button=findViewById(R.id.btn_signup);

        mAuth=FirebaseAuth.getInstance();
        //FirebaseUser user=mAuth.getCurrentUser();
        /*if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(HomeActivity.this,LivePreviewActivity.class));
            finish();
        }*/
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                if(TextUtils.isEmpty(Email)){
                    email.setError("Fill Email field");
                    email.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    email.setError("Please enter a valid Email");
                    email.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(Password)){
                    password.setError("Fill password field");
                    password.requestFocus();
                    return;
                }
                else if(Password.length()<6)
                {
                    password.setError("Password length is <6");
                    password.requestFocus();
                    return;
                }
                else {
                    mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "User Login Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(HomeActivity.this, botton_nav.class));
                            } else {
                                Toast.makeText(HomeActivity.this, "User Login UnSuccessfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,RegistrationActivity.class));
            }
        });
    }
}