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

package com.google.mlkit.vision.demo.java;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.demo.CameraSource;
import com.google.mlkit.vision.demo.CameraSourcePreview;
import com.google.mlkit.vision.demo.GraphicOverlay;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.java.posedetector.BicepCurl;
import com.google.mlkit.vision.demo.java.posedetector.PoseDetectorProcessor;
import com.google.mlkit.vision.demo.java.posedetector.ShoulderPress;
import com.google.mlkit.vision.demo.java.posedetector.Squats;
import com.google.mlkit.vision.demo.java.posedetector.Workout;
import com.google.mlkit.vision.demo.preference.PreferenceUtils;
import com.google.mlkit.vision.demo.preference.SettingsActivity;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@KeepName
public final class LivePreviewActivity extends AppCompatActivity
    implements OnRequestPermissionsResultCallback,
        OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {
  private static final String BICEP_CURL = "BICEP CURLS";
  private static final String SQUATS = "SQUATS";
  private static final String SHOULDER_PRESS = "SHOULDER PRESS";

  private static final String TAG = "LivePreviewActivity";
  private static final int PERMISSION_REQUESTS = 1;
  public static int selectedExercise= -1;
  private CameraSource cameraSource = null;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private String selectedModel = BICEP_CURL;
  private MediaPlayer mMediaplayer;
  private AlertDialog.Builder dialogBuilder;
  private AlertDialog dialog;
  private EditText numberOfSets, numberOfReps;
  private Button Continue;
  public static int numOfSets=-1;
  public static int numOfReps=-1;


  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate");

    setContentView(R.layout.activity_vision_live_preview);

    mMediaplayer=MediaPlayer.create(this,R.raw.music);

    mMediaplayer.start();
    preview = findViewById(R.id.preview_view);
    if (preview == null) {
      Log.d(TAG, "Preview is null");
    }
    graphicOverlay = findViewById(R.id.graphic_overlay);
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null");
    }

    Spinner spinner = findViewById(R.id.spinner);
    List<String> options = new ArrayList<>();
    options.add(BICEP_CURL);
    options.add(SQUATS);
    options.add(SHOULDER_PRESS);

///
    //options.add("Deadlift");

    // Creating adapter for spinner
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_style, options);
    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    spinner.setAdapter(dataAdapter);
    spinner.setOnItemSelectedListener(this);

    ToggleButton facingSwitch = findViewById(R.id.facing_switch);
    facingSwitch.setOnCheckedChangeListener(this);

    ImageView settingsButton = findViewById(R.id.settings_button);
    settingsButton.setOnClickListener(
        v -> {
          mMediaplayer.pause();
          Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
          intent.putExtra(
              SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW);
          startActivity(intent);
        });

    if (allPermissionsGranted()) {
      createCameraSource(selectedModel);
    } else {
      getRuntimePermissions();
    }

    dialogBuilder = new AlertDialog.Builder(this);
    final View setInfoView= getLayoutInflater().inflate(R.layout.popup, null);
    numberOfSets= setInfoView.findViewById(R.id.setsInfo);
    numberOfReps= setInfoView.findViewById(R.id.repsInSet);
    Continue= (Button) setInfoView.findViewById(R.id.cont);

    dialogBuilder.setView(setInfoView);
    dialog= dialogBuilder.create();

  }

  public void getSetsInfo(){

    dialog.show();
      Continue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String sets=numberOfSets.getText().toString().trim();
        String reps=numberOfReps.getText().toString().trim();

        if(TextUtils.isEmpty(sets)){
          numberOfSets.setError("Sets Required");
          return;
        }
        else if(TextUtils.isEmpty(reps)){
          numberOfReps.setError("Repetitions Required");
          return;
        }
        else{
          numOfSets=Integer.parseInt(sets);
          numOfReps= Integer.parseInt(reps);
          dialog.dismiss();
        }
      }
    });



  }

  private void releaseMediaPlayer(){
    if(mMediaplayer!=null){
      mMediaplayer.release();
      mMediaplayer=null;
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public synchronized void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent.getItemAtPosition(pos).toString();
    Log.d(TAG, "Selected model: " + selectedModel);
    preview.stop();
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel);
      startCameraSource();
    } else {
      getRuntimePermissions();
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Log.d(TAG, "Set facing");
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
      } else {
        cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
      }
    }
    preview.stop();
    startCameraSource();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void createCameraSource(String model) {
    if (cameraSource == null) {
      cameraSource = new CameraSource(this, graphicOverlay);
    }

    try {
      switch (model) {
        case BICEP_CURL: {
          if(selectedExercise!=-1 && (BicepCurl.counter !=0 || ShoulderPress.counter!=0 || Squats.counter!=0)){
            Workout wObj= null;
            if (selectedExercise==2) {
              wObj = new Workout(Squats.counter, "Squats", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), Squats.counter / numOfReps);
              Squats.counter=0;
            }
            else if (selectedExercise==3) {
              wObj = new Workout(ShoulderPress.counter, "Shoulder Press", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), ShoulderPress.counter / numOfReps);
              ShoulderPress.counter=0;
            }

            DatabaseReference dOBJ=FirebaseDatabase.getInstance().getReference("Workouts history2");
            String key = dOBJ.push().getKey();

            dOBJ.child(key).setValue(wObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                  @Override
                                                                  public void onSuccess(@NonNull Void unused) {
                                                                    Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
                                                                  }
                                                                }
            ).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
              }
            });

          }



          selectedExercise =1;
          getSetsInfo();
          PoseDetectorOptionsBase poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
          Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
          boolean shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
          boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
          boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
          boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
          cameraSource.setMachineLearningFrameProcessor(
                  new PoseDetectorProcessor(
                          this,
                          poseDetectorOptions,
                          shouldShowInFrameLikelihood,
                          visualizeZ,
                          rescaleZ,
                          runClassification,
                          /* isStreamMode = */ true));
          break;
        }
        case SQUATS: {
          if(selectedExercise!=-1){
            Workout wObj= null;
            if (selectedExercise==1 && (BicepCurl.counter !=0 || ShoulderPress.counter!=0 || Squats.counter!=0) ) {
              wObj = new Workout(BicepCurl.counter, "Bicep Curl", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), BicepCurl.counter / numOfReps);
              BicepCurl.counter=0;
            }
            else if (selectedExercise==3) {
              wObj = new Workout(ShoulderPress.counter, "Shoulder Press", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), ShoulderPress.counter / numOfReps);
              ShoulderPress.counter=0;
            }

            DatabaseReference dOBJ=FirebaseDatabase.getInstance().getReference("Workouts history2");
            String key = dOBJ.push().getKey();

            dOBJ.child(key).setValue(wObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                  @Override
                                                                  public void onSuccess(@NonNull Void unused) {
                                                                    Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
                                                                  }
                                                                }
            ).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
              }
            });

          }



          selectedExercise =2;
          getSetsInfo();
          PoseDetectorOptionsBase poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
          Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
          boolean shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
          boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
          boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
          boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
          cameraSource.setMachineLearningFrameProcessor(
                  new PoseDetectorProcessor(
                          this,
                          poseDetectorOptions,
                          shouldShowInFrameLikelihood,
                          visualizeZ,
                          rescaleZ,
                          runClassification,
                          /* isStreamMode = */ true));
          break;
        }

        case SHOULDER_PRESS: {

          if(selectedExercise!=-1 && (BicepCurl.counter !=0 || ShoulderPress.counter!=0 || Squats.counter!=0)){
            Workout wObj= null;
            if (selectedExercise==2) {
              wObj = new Workout(Squats.counter, "Squats", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), Squats.counter / numOfReps);
              Squats.counter=0;
            }
            else if (selectedExercise==1) {
              wObj = new Workout(BicepCurl.counter, "Bicep Curl", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), BicepCurl.counter / numOfReps);
              BicepCurl.counter=0;
            }

            DatabaseReference dOBJ=FirebaseDatabase.getInstance().getReference("Workouts history2");
            String key = dOBJ.push().getKey();

            dOBJ.child(key).setValue(wObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                  @Override
                                                                  public void onSuccess(@NonNull Void unused) {
                                                                    Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
                                                                  }
                                                                }
            ).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
              }
            });

          }
          selectedExercise =3;
          getSetsInfo();
          PoseDetectorOptionsBase poseDetectorOptions =
                  PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
          Log.i(TAG, "Using Pose Detector with options " + poseDetectorOptions);
          boolean shouldShowInFrameLikelihood =
                  PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
          boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
          boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
          boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
          cameraSource.setMachineLearningFrameProcessor(
                  new PoseDetectorProcessor(
                          this,
                          poseDetectorOptions,
                          shouldShowInFrameLikelihood,
                          visualizeZ,
                          rescaleZ,
                          runClassification,
                          /* isStreamMode = */ true));
          break;
        }

        default:
          Log.e(TAG, "Unknown model: " + model);
      }
    } catch (RuntimeException e) {
      Log.e(TAG, "Can not create image processor: " + model, e);
      Toast.makeText(
              getApplicationContext(),
              "Can not create image processor: " + e.getMessage(),
              Toast.LENGTH_LONG)
          .show();
    }
  }

  private void startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null");
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null");
        }
        preview.start(cameraSource, graphicOverlay);
      } catch (IOException e) {
        Log.e(TAG, "Unable to start camera source.", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume");
    createCameraSource(selectedModel);
    startCameraSource();
    if(mMediaplayer!= null){
      mMediaplayer.start();
    }
  }

  /** Stops the camera. */
  @Override
  protected void onPause() {
    super.onPause();
    preview.stop();
    if(mMediaplayer!= null){
      mMediaplayer.pause();
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
      Log.d("ADebugTag", "Counter issssssssssssssss: " + Integer.toString(BicepCurl.counter));
      //BicepCurl.counter

      if(BicepCurl.counter !=0 || ShoulderPress.counter!=0 || Squats.counter!=0){
        Workout wObj= null;
        if (selectedExercise==1) {
          wObj = new Workout(BicepCurl.counter, "Bicep Curl", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), BicepCurl.counter / numOfReps);
          BicepCurl.counter=0;
        }
        else if (selectedExercise==2) {
          wObj = new Workout(Squats.counter, "Squats", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), Squats.counter / numOfReps);
          Squats.counter=0;
        }
        else if (selectedExercise==3) {
          wObj = new Workout(ShoulderPress.counter, "Shoulder Press", LocalDate.now().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), ShoulderPress.counter / numOfReps);
          ShoulderPress.counter=0;
        }

        DatabaseReference dOBJ=FirebaseDatabase.getInstance().getReference("Workouts history2");
        String key = dOBJ.push().getKey();

        dOBJ.child(key).setValue(wObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(@NonNull Void unused) {
                                                                Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
                                                              }
                                                            }
        ).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
          }
        });

      }

    }
    releaseMediaPlayer();
  }

  private String[] getRequiredPermissions() {
    try {
      PackageInfo info =
          this.getPackageManager()
              .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
      String[] ps = info.requestedPermissions;
      if (ps != null && ps.length > 0) {
        return ps;
      } else {
        return new String[0];
      }
    } catch (Exception e) {
      return new String[0];
    }
  }

  private boolean allPermissionsGranted() {
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        return false;
      }
    }
    return true;
  }

  private void getRuntimePermissions() {
    List<String> allNeededPermissions = new ArrayList<>();
    for (String permission : getRequiredPermissions()) {
      if (!isPermissionGranted(this, permission)) {
        allNeededPermissions.add(permission);
      }
    }

    if (!allNeededPermissions.isEmpty()) {
      ActivityCompat.requestPermissions(
          this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    Log.i(TAG, "Permission granted!");
    if (allPermissionsGranted()) {
      createCameraSource(selectedModel);
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private static boolean isPermissionGranted(Context context, String permission) {
    if (ContextCompat.checkSelfPermission(context, permission)
        == PackageManager.PERMISSION_GRANTED) {
      Log.i(TAG, "Permission granted: " + permission);
      return true;
    }
    Log.i(TAG, "Permission NOT granted: " + permission);
    return false;
  }



}
