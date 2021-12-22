package com.google.mlkit.vision.demo.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mlkit.vision.demo.R;

public class BMI_calculation extends AppCompatActivity {
    EditText age,height,weight;
    TextView bmi_val,bmi_comment;
    Button calculate_button;
    ImageView signal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_calculation);
        getSupportActionBar().hide();

        age=findViewById(R.id.tv_age_num);
        height=findViewById(R.id.tv_height_num);
        weight=findViewById(R.id.tv_weight_num);
        bmi_val=findViewById(R.id.tv_bmi_value);
        bmi_comment=findViewById(R.id.tv_bmi_comment);
        calculate_button=findViewById(R.id.btn_calculate_bmi);
        signal=findViewById(R.id.bmi_signal);

        calculate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age_val=age.getText().toString().trim();
                String height_val=height.getText().toString().trim();
                String weight_val=weight.getText().toString().trim();
                if(TextUtils.isEmpty(age_val)){
                    age.setError("Age Required");
                    return;
                }
                else if(TextUtils.isEmpty(height_val)){
                    age.setError("Height Required");
                    return;
                }
                else if(TextUtils.isEmpty(weight_val)){
                    age.setError("Weight Required");
                    return;
                }
                else{
                    Float height_value=Float.parseFloat(height_val)/100;
                    Float weight_value=Float.parseFloat(weight_val);
                    Float bmi_value=weight_value/((height_value*height_value));

                    displayBMI(bmi_value);
                }
            }
        });
    }
    protected void displayBMI(Float bmi){
        String BMI_comment;
        if (Float.compare(bmi, 15f) <= 0) {
            BMI_comment = "very severely underweight";
            signal.setImageResource(R.drawable.ic_deepred_circle);
        } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
            BMI_comment = "severely underweight";
            signal.setImageResource(R.drawable.ic_deepred_circle);
        } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
            BMI_comment = "underweight";
            signal.setImageResource(R.drawable.ic_yellow_circle);
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            BMI_comment = "Normal";
            signal.setImageResource(R.drawable.ic_green_circle);
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            BMI_comment = "Overweight";
            signal.setImageResource(R.drawable.ic_yellow_circle);
        } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
            BMI_comment = "Obese Class 1";
            signal.setImageResource(R.drawable.ic_deepred_circle);
        } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
            BMI_comment = "Obese Class 2";
            signal.setImageResource(R.drawable.ic_deepred_circle);
        } else {
            BMI_comment = "Obese Class 3";
            signal.setImageResource(R.drawable.ic_deepred_circle);
        }
        signal.setVisibility(View.VISIBLE);
        bmi_comment.setText(BMI_comment);
        bmi_val.setText(bmi.toString());

    }
}