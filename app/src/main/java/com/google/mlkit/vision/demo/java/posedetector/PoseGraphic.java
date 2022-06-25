/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.java.posedetector;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.common.primitives.Ints;
import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.GraphicOverlay.Graphic;
import com.google.mlkit.vision.demo.HomeFragment;
import com.google.mlkit.vision.demo.java.LivePreviewActivity;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Draw the detected pose in preview. */
public class PoseGraphic extends Graphic {
  LivePreviewActivity LiveActivity;
  private static final float DOT_RADIUS = 8.0f;
  private static final float IN_FRAME_LIKELIHOOD_TEXT_SIZE = 30.0f;
  private static final float STROKE_WIDTH = 10.0f;
  private static final float POSE_CLASSIFICATION_TEXT_SIZE = 60.0f;

  private final Pose pose;
  private final boolean showInFrameLikelihood;
  private final boolean visualizeZ;
  private final boolean rescaleZForVisualization;
  private float zMin = Float.MAX_VALUE;
  private float zMax = Float.MIN_VALUE;

  private final List<String> poseClassification;
  private final Paint classificationTextPaint;
  private final Paint leftPaint;
  private final Paint rightPaint;
  private final Paint whitePaint;

  PoseGraphic(
      GraphicOverlay overlay,
      Pose pose,
      boolean showInFrameLikelihood,
      boolean visualizeZ,
      boolean rescaleZForVisualization,
      List<String> poseClassification) {
    super(overlay);
    this.pose = pose;
    this.showInFrameLikelihood = showInFrameLikelihood;
    this.visualizeZ = visualizeZ;
    this.rescaleZForVisualization = rescaleZForVisualization;

    this.poseClassification = poseClassification;
    classificationTextPaint = new Paint();
    classificationTextPaint.setColor(Color.WHITE);
    classificationTextPaint.setTextSize(POSE_CLASSIFICATION_TEXT_SIZE);
    classificationTextPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);

    whitePaint = new Paint();
    whitePaint.setStrokeWidth(STROKE_WIDTH);
    whitePaint.setColor(Color.WHITE);
    whitePaint.setTextSize(IN_FRAME_LIKELIHOOD_TEXT_SIZE);
    leftPaint = new Paint();
    leftPaint.setStrokeWidth(STROKE_WIDTH);
    leftPaint.setColor(Color.GREEN);
    rightPaint = new Paint();
    rightPaint.setStrokeWidth(STROKE_WIDTH);
    rightPaint.setColor(Color.RED);
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void draw(Canvas canvas) {
    List<PoseLandmark> landmarks = pose.getAllPoseLandmarks();
    if (landmarks.isEmpty()) {
      return;
    }

    // Draw pose classification text.
    float classificationX = POSE_CLASSIFICATION_TEXT_SIZE * 0.5f;
    for (int i = 0; i < poseClassification.size(); i++) {
      float classificationY = (canvas.getHeight() - POSE_CLASSIFICATION_TEXT_SIZE * 1.5f
          * (poseClassification.size() - i));
      canvas.drawText(
          poseClassification.get(i),
          classificationX,
          classificationY,
          classificationTextPaint);
    }

    // Draw all the points
    for (PoseLandmark landmark : landmarks) {
      drawPoint(canvas, landmark, whitePaint);
      if (visualizeZ && rescaleZForVisualization) {
        zMin = min(zMin, landmark.getPosition3D().getZ());
        zMax = max(zMax, landmark.getPosition3D().getZ());
      }
    }

    ArrayList<PoseLandmark> points= new ArrayList<>();
    PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
    points.add(leftShoulder);
    PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
    points.add(rightShoulder);
    PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
    points.add(leftElbow);
    PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
    points.add(rightElbow);
    PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
    points.add(leftWrist);
    PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
    points.add(rightWrist);
    PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
    points.add(leftHip);
    PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
    points.add(rightHip);

// onwards 8 squats
    PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
    //points.add(nose);
    PoseLandmark lefyEyeInner = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_INNER);
    //points.add(lefyEyeInner);
    PoseLandmark lefyEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
    //points.add(lefyEye);
    PoseLandmark leftEyeOuter = pose.getPoseLandmark(PoseLandmark.LEFT_EYE_OUTER);
    //points.add(leftEyeOuter);
    PoseLandmark rightEyeInner = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_INNER);
    //points.add(rightEyeInner);
    PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
    //points.add(rightEye);
    PoseLandmark rightEyeOuter = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE_OUTER);
    //points.add(rightEyeOuter);
    PoseLandmark leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
    //points.add(leftEar);
    PoseLandmark rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
    //points.add(rightEar);
    PoseLandmark leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH);
    //points.add(leftMouth);
    PoseLandmark rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);
    //points.add(rightMouth);
    PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
    points.add(leftKnee);
    PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
    //points.add(rightKnee);
    PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
    points.add(leftAnkle);
    PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);
    //points.add(rightAnkle);
    PoseLandmark leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY);
    //points.add(leftPinky);
    PoseLandmark rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY);
    //points.add(rightPinky);
    PoseLandmark leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
    //points.add(leftIndex);
    PoseLandmark rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
    //points.add(rightIndex);
    PoseLandmark leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB);
    //points.add(leftThumb);
    PoseLandmark rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB);
    //points.add(rightThumb);
    PoseLandmark leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL);
    //points.add(leftHeel);
    PoseLandmark rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL);
    //points.add(rightHeel);
    PoseLandmark leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX);
    points.add(leftFootIndex);
    PoseLandmark rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX);
    //points.add(rightFootIndex);
    boolean flag = false;
    if(LivePreviewActivity.selectedExercise==1) {
      BicepCurl obj = new BicepCurl(points, canvas, rightPaint);

      flag = obj.processAngels();
    }

    else if(LivePreviewActivity.selectedExercise==2) {
      Squats obj = new Squats(points, canvas, rightPaint);

      flag = obj.processAngels();
    }

    else if(LivePreviewActivity.selectedExercise==3) {
      ShoulderPress obj = new ShoulderPress(points, canvas, rightPaint);

      flag = obj.processAngels();
    }
    if (flag ==true){
//      Intent intent = new Intent();
//      intent.setClass(LivePreviewActivity.getContext(), HomeFragment.class);
//      LivePreviewActivity.getContext().startActivity(intent);
      //LiveActivity.onBackPressed();

    }
    // Face
    drawLine(canvas, nose, lefyEyeInner, leftPaint);
    drawLine(canvas, lefyEyeInner, lefyEye, leftPaint);
    drawLine(canvas, lefyEye, leftEyeOuter, leftPaint);
    drawLine(canvas, leftEyeOuter, leftEar, leftPaint);
    drawLine(canvas, nose, rightEyeInner, leftPaint);
    drawLine(canvas, rightEyeInner, rightEye, whitePaint);
    drawLine(canvas, rightEye, rightEyeOuter, whitePaint);
    drawLine(canvas, rightEyeOuter, rightEar, whitePaint);
    drawLine(canvas, leftMouth, rightMouth, whitePaint);

    drawLine(canvas, leftShoulder, rightShoulder, whitePaint);
    drawLine(canvas, leftHip, rightHip, whitePaint);


      drawLine(canvas, leftShoulder, leftElbow, leftPaint);
      drawLine(canvas, leftElbow, leftWrist, leftPaint);
      drawLine(canvas, leftShoulder, leftHip, leftPaint);
      drawLine(canvas, rightShoulder, rightElbow, leftPaint);
      drawLine(canvas, rightElbow, rightWrist, leftPaint);
      drawLine(canvas, rightShoulder, rightHip, leftPaint);

      drawLine(canvas, leftShoulder, leftElbow, rightPaint);
      drawLine(canvas, leftElbow, leftWrist, rightPaint);
      drawLine(canvas, leftShoulder, leftHip, rightPaint);

      drawLine(canvas, rightShoulder, rightElbow, rightPaint);
      drawLine(canvas, rightElbow, rightWrist, rightPaint);
      drawLine(canvas, rightShoulder, rightHip, rightPaint);


    drawLine(canvas, leftHip, leftKnee, whitePaint);
    drawLine(canvas, leftKnee, leftAnkle, whitePaint);
    drawLine(canvas, leftWrist, leftThumb, whitePaint);
    drawLine(canvas, leftWrist, leftPinky, whitePaint);
    drawLine(canvas, leftWrist, leftIndex, whitePaint);
    drawLine(canvas, leftIndex, leftPinky, whitePaint);
    drawLine(canvas, leftAnkle, leftHeel, whitePaint);
    drawLine(canvas, leftHeel, leftFootIndex, whitePaint);

    // Right body
    drawLine(canvas, rightHip, rightKnee, whitePaint);
    drawLine(canvas, rightKnee, rightAnkle, whitePaint);
    drawLine(canvas, rightWrist, rightThumb, whitePaint);
    drawLine(canvas, rightWrist, rightPinky, whitePaint);
    drawLine(canvas, rightWrist, rightIndex, whitePaint);
    drawLine(canvas, rightIndex, rightPinky, whitePaint);
    drawLine(canvas, rightAnkle, rightHeel, whitePaint);
    drawLine(canvas, rightHeel, rightFootIndex, whitePaint);

    // Draw inFrameLikelihood for all points
    if (showInFrameLikelihood) {
      for (PoseLandmark landmark : landmarks) {
        canvas.drawText(
            String.format(Locale.US, "%.2f", landmark.getInFrameLikelihood()),
            translateX(landmark.getPosition().x),
            translateY(landmark.getPosition().y),
            whitePaint);
      }
    }
  }

    void drawPoint(Canvas canvas, PoseLandmark landmark, Paint paint) {
    PointF3D point = landmark.getPosition3D();
    maybeUpdatePaintColor(paint, canvas, point.getZ());
    canvas.drawCircle(translateX(point.getX()), translateY(point.getY()), DOT_RADIUS, paint);
  }

  void drawLine(Canvas canvas, PoseLandmark startLandmark, PoseLandmark endLandmark, Paint paint) {
    PointF3D start = startLandmark.getPosition3D();
    PointF3D end = endLandmark.getPosition3D();

    // Gets average z for the current body line
    float avgZInImagePixel = (start.getZ() + end.getZ()) / 2;
    maybeUpdatePaintColor(paint, canvas, avgZInImagePixel);

    canvas.drawLine(
          translateX(start.getX()),
          translateY(start.getY()),
          translateX(end.getX()),
          translateY(end.getY()),
          paint);
  }

  private void maybeUpdatePaintColor(Paint paint, Canvas canvas, float zInImagePixel) {
    if (!visualizeZ) {
      return;
    }

    // When visualizeZ is true, sets up the paint to different colors based on z values.
    // Gets the range of z value.
    float zLowerBoundInScreenPixel;
    float zUpperBoundInScreenPixel;

    if (rescaleZForVisualization) {
      zLowerBoundInScreenPixel = min(-0.001f, scale(zMin));
      zUpperBoundInScreenPixel = max(0.001f, scale(zMax));
    } else {
      // By default, assume the range of z value in screen pixel is [-canvasWidth, canvasWidth].
      float defaultRangeFactor = 1f;
      zLowerBoundInScreenPixel = -defaultRangeFactor * canvas.getWidth();
      zUpperBoundInScreenPixel = defaultRangeFactor * canvas.getWidth();
    }

    float zInScreenPixel = scale(zInImagePixel);

    if (zInScreenPixel < 0) {
      // Sets up the paint to draw the body line in red if it is in front of the z origin.
      // Maps values within [zLowerBoundInScreenPixel, 0) to [255, 0) and use it to control the
      // color. The larger the value is, the more red it will be.
      int v = (int) (zInScreenPixel / zLowerBoundInScreenPixel * 255);
      v = Ints.constrainToRange(v, 0, 255);
      paint.setARGB(255, 255, 255 - v, 255 - v);
    } else {
      // Sets up the paint to draw the body line in blue if it is behind the z origin.
      // Maps values within [0, zUpperBoundInScreenPixel] to [0, 255] and use it to control the
      // color. The larger the value is, the more blue it will be.
      int v = (int) (zInScreenPixel / zUpperBoundInScreenPixel * 255);
      v = Ints.constrainToRange(v, 0, 255);
      paint.setARGB(255, 255 - v, 255 - v, 255);
    }
  }
}
