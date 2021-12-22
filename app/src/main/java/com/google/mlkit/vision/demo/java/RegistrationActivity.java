package com.google.mlkit.vision.demo.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.demo.R;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,email,password,age,height,weight;
    Button register_button;
    FirebaseAuth mAuth;
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

        mAuth=FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name=name.getText().toString().trim();
                String Email=email.getText().toString().trim();
                String Password=password.getText().toString().trim();
                String Age=age.getText().toString().trim();
                String Height=height.getText().toString().trim();
                String Weight=weight.getText().toString().trim();

                if(TextUtils.isEmpty(Name)){
                    name.setError("Fill Name field");
                    return;
                }
                else if(TextUtils.isEmpty(Email)){
                    email.setError("Fill Email field");
                    return;
                }
                else if(TextUtils.isEmpty(Password)){
                    password.setError("Fill password field");
                    return;
                }
                else if(TextUtils.isEmpty(Age)){
                    age.setError("Fill Age field");
                    return;
                }
                else if(TextUtils.isEmpty(Height)){
                    height.setError("Fill Height field");
                    return;
                }
                else if(TextUtils.isEmpty(Weight)){
                    weight.setError("Fill Weight field");
                    return;
                }
                else{
                    mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                            }else{
                                Toast.makeText(RegistrationActivity.this,"User Not Registered",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });
    }
}