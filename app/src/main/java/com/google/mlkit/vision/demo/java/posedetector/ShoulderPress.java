package com.google.mlkit.vision.demo.java.posedetector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;

public class ShoulderPress {

    ArrayList<PoseLandmark> poses;
    Canvas canvas;
    Paint paint;
    static boolean stopFlag = false;
    static String stage="down";
    public static int counter=0;
    public static boolean exerciseStopFlag= false;
    private static final float TEXT_SIZE = 80.0f;
    float x = TEXT_SIZE * 0.5f;
    float y = TEXT_SIZE * 1.5f;
    public static int negCounter=0;
    public static int dummyCount=-1;
    static boolean armFlag=false;
    public ShoulderPress(ArrayList<PoseLandmark> poses, Canvas c, Paint color){
        this.poses= poses;
        canvas= c;
        paint=color;
        paint.setTextSize(100f);
    }
    public double calculateAngles(PointF3D first, PointF3D second, PointF3D third,double constant){
        double temp1= Math.atan2(third.getY()/constant-second.getY()/constant, third.getX()/constant- second.getX()/constant);
        double temp2= Math.atan2(first.getY()/constant-second.getY()/constant, first.getX()/constant- second.getX()/constant);
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

//        double left_angle_for_curl=calculateAngles(leftShoulderValues,leftElbowValues,leftWristValues);
//        double left_angle_for_tuck=calculateAngles(leftHipValues,leftShoulderValues,leftElbowValues);
        //Log.d("ADebugTag", "left Angel for Tuck: " + Double.toString(left_angle_for_tuck));


        double distance_shoulder_elbow_left=(leftElbowValues.getY()-leftShoulderValues.getY());
        double distance_hip_shoulder_left=(leftHipValues.getY()-leftShoulderValues.getY());

        double right_angle_for_curl=calculateAngles(rightShoulderValues,rightElbowValues,rightWristValues,distance_hip_shoulder_left);
        double right_angle_for_tuck=calculateAngles(rightHipValues,rightShoulderValues,rightElbowValues,distance_hip_shoulder_left);

        double distance_shoulder_elbow_right=(rightElbowValues.getY()-rightShoulderValues.getY());
        double distance_hip_shoulder_right=(rightHipValues.getY()-rightShoulderValues.getY());
        double distance_wrist=(rightWristValues.getY()-leftWristValues.getY());
        double distance=Math.sqrt((rightWristValues.getY() - leftWristValues.getY()) * (rightWristValues.getY() - leftWristValues.getY()) + (rightWristValues.getX() - leftWristValues.getX()) * (rightWristValues.getX() - leftWristValues.getX()));
        double distance_ratio=Math.sqrt((rightShoulderValues.getY() - rightHipValues.getY()) * (rightShoulderValues.getY() - rightHipValues.getY()) + (rightShoulderValues.getX() - rightHipValues.getX()) * (rightShoulderValues.getX() - rightHipValues.getX()));


        double ratioL= distance/distance_ratio;
        //double ratioR= distance_shoulder_elbow_right/distance_hip_shoulder_right;

       // Log.d(String.valueOf(right_angle_for_tuck), "processAngels: ");
        Log.d(String.valueOf(distance/distance_ratio), "p----------------------: ");
        if(exerciseStopFlag ==false) {
            if (right_angle_for_tuck <= 105 && right_angle_for_curl <= 105) {
                armFlag=false;
                stopFlag = true;
                stage = "down";
            } else {
                stopFlag = false;

            }

            if (stopFlag == false && right_angle_for_tuck < 100 && right_angle_for_curl < 110) {
                stage = "down";
            }
            if (ratioL > 1.5) {
                canvas.drawText("Bring Arm Closer", x + 50, y + TEXT_SIZE * 2, paint);
                armFlag=true;
                if (dummyCount == -1) {

                    negCounter++;
                    dummyCount = 0;
                }
            }
            if (ratioL < 1.1) {
                if (stage == "down")
                    canvas.drawText("Make Arm Wider", x + 50, (y + TEXT_SIZE * 3) + 10, paint);
                if (dummyCount == -1) {
                    negCounter++;
                    dummyCount = 0;
                }
            }


            if (stopFlag == false && right_angle_for_tuck >= 130 && right_angle_for_curl >= 145 && stage == "down" &&armFlag==false) {
                stage = "up";
                counter = counter + 1;
                dummyCount = -1;
            }
            //canvas.drawText("Sets: "+ Integer.toString(counter/ LivePreviewActivity.numOfReps), 400,250,paint);
            //canvas.drawText("Counter: "+ Integer.toString(counter), 300,450,paint);

            //canvas.drawText(stage, 120,350,paint);
            if (ShoulderPress.counter == LivePreviewActivity.numOfReps * LivePreviewActivity.numOfSets) {
                exerciseStopFlag= true;
                return true;
            }
        }
        return exerciseStopFlag;
    }
}
