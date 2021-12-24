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

public class bodyfat_calculation extends AppCompatActivity {
    Button btn_calculate_bf;
    EditText age, height, weight, sex;
    TextView bf_val, bf_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodyfat_calculation);

        btn_calculate_bf = findViewById(R.id.btn_calculate_bf);
        age = findViewById(R.id.tv_age_bf_num);
        height = findViewById(R.id.tv_height_bf_num);
        weight = findViewById(R.id.tv_weight_bf_num);
        sex = findViewById(R.id.tv_sex);
        bf_val = findViewById(R.id.tv_bf_value);
        bf_comment = findViewById(R.id.tv_bf_comment);

        btn_calculate_bf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age_val = age.getText().toString().trim();
                String height_val = height.getText().toString().trim();
                String weight_val = weight.getText().toString().trim();
                if (TextUtils.isEmpty(age_val)) {
                    age.setError("Age Required");
                } else if (TextUtils.isEmpty(height_val)) {
                    age.setError("Height Required");
                } else if (TextUtils.isEmpty(weight_val)) {
                    age.setError("Weight Required");
                } else {
                    Float height_value = Float.parseFloat(height_val) / 100;
                    Float weight_value = Float.parseFloat(weight_val);
                    Float bmi_value = weight_value / ((height_value * height_value));

                    double body_fat = (1.20 * bmi_value) + (0.23 * Float.parseFloat(age_val)) - 16.2;
                    bf_val.setText(String.format("Body Fat Percentage: %.2f", body_fat));

                    display_BF(sex.getText().toString(), Float.parseFloat(age_val), body_fat);
                }
            }
        });
    }

    protected void display_BF(String sex, Float age, double body_fat) {
        if (sex.contains("M")) {
            //20-40 yrs old: Underfat: under 8 percent, Healthy: 8-19 percent, Overweight: 19-25 percent, Obese: over 25 percent
            if (age >= 20 && age <= 40) {
                if (body_fat < 8) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 8 && body_fat < 19) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");

                } else if (body_fat >= 19 && body_fat < 25) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }
            }
            //41-60 yrs old: Underfat: under 11 percent, Healthy: 11-22 percent, Overweight: 22-27 percent, Obese: over 27 percent
            else if (age > 40 && age < 60) {
                if (body_fat < 11) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 11 && body_fat < 22) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");
                } else if (body_fat >= 22 && body_fat < 27) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }
            }
            //61-79 yrs old: Underfat: under 13 percent, Healthy: 13-25 percent, Overweight: 25-30 percent, Obese: over 30 percent
            else {
                if (body_fat < 13) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 12 && body_fat < 25) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");
                } else if (body_fat >= 25 && body_fat < 30) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }
            }
        } else {
            //20-40 yrs old: Underfat: under 21 percent, Healthy: 21-33 percent, Overweight: 33-39 percent, Obese: Over 39 percent
            if (age >= 20 && age <= 40) {
                if (body_fat < 21) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 21 && body_fat < 33) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");

                } else if (body_fat >= 33 && body_fat < 39) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }

            }
            //41-60 yrs old: Underfat: under 23 percent, Healthy: 23-35 percent, Overweight : 35-40 percent Obese: over 40 percent
            else if (age > 40 && age < 60) {
                if (body_fat < 23) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 23 && body_fat < 35) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");

                } else if (body_fat >= 35 && body_fat < 40) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }
            }
            //61-79 yrs old: Underfat: under 24 percent, Healthy: 24-36 percent, Overweight: 36-42 percent, Obese: over 42 percent
            else {
                if (body_fat < 24) {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Under Fat");
                } else if (body_fat >= 24 && body_fat < 36) {
                    bf_comment.setTextColor(getResources().getColor(R.color.colorPrimary));
                    bf_comment.setText("Healthy");

                } else if (body_fat >= 36 && body_fat < 42) {
                    bf_comment.setTextColor(getResources().getColor(R.color.yellow));
                    bf_comment.setText("Overweight");
                } else {
                    bf_comment.setTextColor(getResources().getColor(R.color.red));
                    bf_comment.setText("Obese");
                }
            }
        }
    }
}