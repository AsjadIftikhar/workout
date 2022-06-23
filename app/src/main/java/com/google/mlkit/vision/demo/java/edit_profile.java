package com.google.mlkit.vision.demo.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.demo.R;

public class edit_profile extends AppCompatActivity {
    Button prof_update;
    EditText prof_name, prof_email, prof_age, prof_height, prof_pass, prof_new_pass;
    TextView prof_status;
    ProgressBar progressBar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        prof_name = findViewById(R.id.prof_name);
        prof_email = findViewById(R.id.prof_email);
        prof_age = findViewById(R.id.prof_age);
        prof_height = findViewById(R.id.prof_height);
        prof_pass = findViewById(R.id.prof_pass);
        prof_new_pass = findViewById(R.id.prof_new_pass);

        prof_status = findViewById(R.id.prof_status);
        prof_update = findViewById(R.id.pass_update);

        progressBar=findViewById(R.id.progressbar_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile= snapshot.getValue(User.class);

                if (userProfile!=null){
                    String name = userProfile.getName();
                    String email= userProfile.getEmail();
                    String height= userProfile.getHeight();
                    String weight= userProfile.getWeight();
                    String age= userProfile.getAge();
                    String password=userProfile.getPassword();
                    prof_name.setText(name);
                    prof_email.setText(email);
                    prof_age.setText("Age "+age);
                    prof_height.setText("Height "+height);
                    prof_status.setText("Welcome "+name );
                    progressBar.setVisibility(View.INVISIBLE);
                    prof_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String oldPassword= prof_pass.getText().toString();
                            String newPassword= prof_new_pass.getText().toString();
                            if(TextUtils.isEmpty(oldPassword)){
                                prof_pass.setError("Enter Old Password");
                                prof_pass.requestFocus();
                                return;
                            }
                            if(TextUtils.isEmpty(newPassword)){
                                prof_pass.setError("Enter New Password");
                                prof_pass.requestFocus();
                                return;
                            }
                            if(newPassword.length()<6)
                            {
                                prof_new_pass.setError("Password length is <6");
                                prof_new_pass.requestFocus();
                                return;
                            }

                            if(!oldPassword.equals(password)){
                                prof_pass.setError("Wrong Password");
                                prof_pass.requestFocus();
                                return;
                            }
                            else{
                                user.updatePassword(newPassword);
                                reference.child(userID).child("password").setValue(newPassword);
                                Toast.makeText(edit_profile.this,"Password updated successfully",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(edit_profile.this,"Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

    }

}