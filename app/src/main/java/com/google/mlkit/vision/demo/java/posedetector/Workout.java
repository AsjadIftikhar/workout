package com.google.mlkit.vision.demo.java.posedetector;
import java.io.Serializable;



public class Workout{
    private int repititions;
    private String exerciseName;
    private int sets;
    private String feedback;
    @Override
    public String toString() {
        return "Workout{" +
                "repititions=" + repititions +
                ", sets='" + sets + '\'' +
                ", exerciseName='" + exerciseName + '\'' +
                ", feedback='" + feedback + '\'' +
                ", date='" + date + '\'' +
                ", uID='" + uID + '\'' +
                '}';
    }

    public int getRepititions() {
        return repititions;
    }

    public int getSets() {
        return sets;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback=feedback;
    }

    public void setRepititions(int repititions) {
        this.repititions = repititions;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    private String date;
    private String uID;
    public Workout(){}
    public Workout(int repititions, String exerciseName, String date, String uID, int sets, String feedback) {
        this.repititions = repititions;
        this.exerciseName = exerciseName;
        this.date = date;
        this.uID = uID;
        this.sets = sets;
        this.feedback= feedback;
    }

}
