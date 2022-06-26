package com.google.mlkit.vision.demo.java.posedetector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;

public class Squats {

    ArrayList<PoseLandmark> poses;
    Canvas canvas;
    Paint paint;
    public static boolean exerciseStopFlag= false;
    static boolean stopFlag = false;
    static boolean errorFlag=false;
    static String stage="down";
    public static int counter=0;
    private static final float TEXT_SIZE = 80.0f;
    float x = TEXT_SIZE * 0.5f;
    float y = TEXT_SIZE * 1.5f;
    public static int negCounter=0;
    public static int dummyCount=-1;

    public Squats(ArrayList<PoseLandmark> poses, Canvas c, Paint color){
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
        //PoseLandmark leftShoulder= poses.get(0);
        //PoseLandmark rightShoulder= poses.get(1);
        PoseLandmark leftElbow= poses.get(2);
        //PoseLandmark rightElbow= poses.get(3);
        //PoseLandmark leftWrist= poses.get(4);
        //PoseLandmark rightWrist= poses.get(5);
        PoseLandmark leftHip= poses.get(6);
        //PoseLandmark rightHip= poses.get(7);
        PoseLandmark leftknee= poses.get(8);
        PoseLandmark leftankle= poses.get(9);
        PoseLandmark leftfootindex= poses.get(10);

        //PointF3D leftShoulderValues = leftShoulder.getPosition3D();
        //PointF3D rightShoulderValues = rightShoulder.getPosition3D();
        PointF3D leftElbowValues = leftElbow.getPosition3D();
       // PointF3D rightElbowValues = rightElbow.getPosition3D();
        //PointF3D leftWristValues = leftWrist.getPosition3D();
        //PointF3D rightWristValues = rightWrist.getPosition3D();
        PointF3D leftHipValues = leftHip.getPosition3D();
        //PointF3D rightHipValues = rightHip.getPosition3D();
        PointF3D leftkneeValues = leftknee.getPosition3D();
        PointF3D leftankleValues = leftankle.getPosition3D();
        PointF3D leftfootindexValues = leftfootindex.getPosition3D();

//        double left_angle_for_curl=calculateAngles(leftShoulderValues,leftElbowValues,leftWristValues);
//        double left_angle_for_tuck=calculateAngles(leftHipValues,leftShoulderValues,leftElbowValues);
//        //Log.d("ADebugTag", "left Angel for Tuck: " + Double.toString(left_angle_for_tuck));
//        double right_angle_for_curl=calculateAngles(rightShoulderValues,rightElbowValues,rightWristValues);
//        double right_angle_for_tuck=calculateAngles(rightHipValues,rightShoulderValues,rightElbowValues);
//        double distance_shoulder_elbow_left=(leftElbowValues.getY()-leftShoulderValues.getY());
//        double distance_hip_shoulder_left=(leftHipValues.getY()-leftShoulderValues.getY());
//        double distance_shoulder_elbow_right=(rightElbowValues.getY()-rightShoulderValues.getY());
//        double distance_hip_shoulder_right=(rightHipValues.getY()-rightShoulderValues.getY());
//
//        //squats
        double distance_leftKnee_leftAnkle=(leftkneeValues.getY()-leftankleValues.getY());
//        double distance_knee_left=(leftElbowValues.getY()-leftShoulderValues.getY());
        double angle_left_squat=calculateAngles(leftankleValues,leftkneeValues,leftHipValues,distance_leftKnee_leftAnkle);
        double angle_error_squat=calculateAngles(leftkneeValues,leftankleValues,leftfootindexValues,distance_leftKnee_leftAnkle);

        Log.d(String.valueOf(angle_left_squat), "processAngels: ");
        //Log.d(String.valueOf(angle_error_squat), "processAngels: ");



//        double ratioL= distance_shoulder_elbow_left/distance_hip_shoulder_left;
        //double ratioR= /distance_hip_shoulder_right;
        if(exerciseStopFlag ==false) {
            if (angle_left_squat >= 120) {
                stopFlag = true;
                errorFlag=false;
                //canvas.drawText("You are in Squats", 120,350,paint);
                stage = "up";
            } else {
                stopFlag = false;

            }
            if (stopFlag == false && angle_left_squat <= 80) {
                if (angle_error_squat >= 125) {
                    canvas.drawText("Knees Moving", x + 50, y + TEXT_SIZE * 2, paint);
                    errorFlag=true;
                    if (dummyCount == -1) {

                        negCounter++;
                        dummyCount = 0;
                    }
                }
                if (angle_left_squat <= 50) {
                    canvas.drawText("Hips too low", x + 50, (y + TEXT_SIZE * 3) + 10, paint);
                    errorFlag=true;
                    if (dummyCount == -1) {
                        negCounter++;
                        dummyCount = 0;

                    }
                }

                stage = "down";

            }
            if (stopFlag == false && angle_left_squat >= 110 && stage == "down"&& errorFlag==false) {
                stage = "up";
                counter = counter + 1;
                dummyCount = -1;
            }
            //canvas.drawText("Sets: "+ Integer.toString(counter/ LivePreviewActivity.numOfReps), 400,250,paint);
            //canvas.drawText("Counter: "+ Integer.toString(counter), 300,450,paint);
            if (Squats.counter == LivePreviewActivity.numOfReps * LivePreviewActivity.numOfSets) {
                exerciseStopFlag = true;
                return true;
            }
        }
        return exerciseStopFlag;
    }
}
