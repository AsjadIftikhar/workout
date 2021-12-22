package com.google.mlkit.vision.demo.java.posedetector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BicepCurl {
    ArrayList<PoseLandmark> poses;
    Canvas canvas;
    Paint paint;
    static boolean stopFlag = false;
    static String stage="down";
    static int counter=0;
    public BicepCurl(ArrayList<PoseLandmark> poses, Canvas c, Paint color){
        this.poses= poses;
        canvas= c;
        paint=color;
        paint.setTextSize(100f);
    }
    public double calculateAngles(PointF3D first, PointF3D second, PointF3D third){
        double temp1= Math.atan2(third.getY()-second.getY(), third.getX()- second.getX());
        double temp2= Math.atan2(first.getY()-second.getY(), first.getX()- second.getX());
        double radian= temp1 -temp2;
        double angle= radian * (180/Math.PI);
        if(angle <0)
        {
            angle=angle*(-1);
        }
        if(angle>180){
            angle =360-angle;
        }
        return angle;
    }

    public boolean processAngels(){
        PoseLandmark leftShoulder= poses.get(0);
        PoseLandmark rightShoulder= poses.get(1);
        PoseLandmark leftElbow= poses.get(2);
        PoseLandmark rightElbow= poses.get(3);
        PoseLandmark leftWrist= poses.get(4);
        PoseLandmark rightWrist= poses.get(5);
        PoseLandmark leftHip= poses.get(6);
        PoseLandmark rightHip= poses.get(7);

        PointF3D leftShoulderValues = leftShoulder.getPosition3D();
        PointF3D rightShoulderValues = rightShoulder.getPosition3D();
        PointF3D leftElbowValues = leftElbow.getPosition3D();
        PointF3D rightElbowValues = rightElbow.getPosition3D();
        PointF3D leftWristValues = leftWrist.getPosition3D();
        PointF3D rightWristValues = rightWrist.getPosition3D();
        PointF3D leftHipValues = leftHip.getPosition3D();
        PointF3D rightHipValues = rightHip.getPosition3D();

        double left_angle_for_curl=calculateAngles(leftShoulderValues,leftElbowValues,leftWristValues);
        double left_angle_for_tuck=calculateAngles(leftHipValues,leftShoulderValues,leftElbowValues);
        //Log.d("ADebugTag", "left Angel for Tuck: " + Double.toString(left_angle_for_tuck));
        double right_angle_for_curl=calculateAngles(rightShoulderValues,rightElbowValues,rightWristValues);
        double right_angle_for_tuck=calculateAngles(rightHipValues,rightShoulderValues,rightElbowValues);
        //Log.d("ADebugTag", "Right Angel for Tuck: " + Double.toString(right_angle_for_tuck));
        double distance_shoulder_elbow_left=(leftElbowValues.getY()-leftShoulderValues.getY());
        double distance_hip_shoulder_left=(leftHipValues.getY()-leftShoulderValues.getY());
        double distance_shoulder_elbow_right=(rightElbowValues.getY()-rightShoulderValues.getY());
        double distance_hip_shoulder_right=(rightHipValues.getY()-rightShoulderValues.getY());

        double ratioL= distance_shoulder_elbow_left/distance_hip_shoulder_left;
        double ratioR= distance_shoulder_elbow_right/distance_hip_shoulder_right;

        if(left_angle_for_tuck>=25 && right_angle_for_tuck>=25) {
            stopFlag= true;
            canvas.drawText("You are flaring out", 120,400,paint);
            stage="up";
      /*      Log.d("ADebugTag", "Stage is: " + stage);
            Log.d("ADebugTag", "Stop Flag is " + Boolean.toString(stopFlag));
      */  }
        else{
            stopFlag =false;
/*
            Log.d("ADebugTag", "Stop Flag is " + Boolean.toString(stopFlag));
            Log.d("ADebugTag", "Stage is: " + stage);
*/

        }
        if(stopFlag==false && left_angle_for_curl>160 && right_angle_for_curl>160){
            stage="down";
/*
            Log.d("ADebugTag", "Stage is: " + stage);
            Log.d("ADebugTag", "Stop Flag is " + Boolean.toString(stopFlag));
*/

        }
        if(ratioL <0.47 && ratioR<0.49) {
            stopFlag = true;
            stage = "up";
          /*  Log.d("ADebugTag", "Stage is: " + stage);
            Log.d("ADebugTag", "Stop Flag is " + Boolean.toString(stopFlag));
*/
            canvas.drawText("Elbows are moving", 120, 400, paint);
        }

        if(stopFlag==false && left_angle_for_curl< 30 && right_angle_for_curl<30 && stage=="down"){
            stage="up";
            counter=counter+1;
            /*Log.d("ADebugTag", "Stage is: " + stage);
            Log.d("ADebugTag", "Stop Flag is " + Boolean.toString(stopFlag));
            Log.d("ADebugTag", "Counter is: " + Integer.toString(counter));
*/
        }
        canvas.drawText("Counter: "+ Integer.toString(counter), 40,40,paint);
        Log.d("ADebugTag", "ratioL: " + Double.toString(ratioL));
        Log.d("ADebugTag", "ratioR " + Double.toString(ratioR));


        //Log.i(leftValues.getX())
        //Log.d("ADebugTag", "ValueX: " + Float.toString(leftValues.getX()));
        //Log.d("ADebugTag", "ValueY: " + Float.toString(leftValues.getY()));

        return true;
    }


}
