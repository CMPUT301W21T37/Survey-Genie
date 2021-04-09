package com.example.surveygenie;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Attributes of Trials and upload the data to the firestore*/
public class Trial implements Serializable {
    private String experimentReference;
    private String experimentType;
    private String result;
    private String user;
    private String location;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Trial(String experimentReference,String experimentType,String result,String user,String location) {
        this.experimentReference = experimentReference;
        this.experimentType = experimentType;
        this.result = result;
        this.user = user;
        this.location = location;
    }

    /*Upload to the firestore*/
    public Trial uploadtodatabase(){
        //may need to implement user later
        Map<String,Object> trialObject = new HashMap<>();
        trialObject.put("Experiment",experimentReference);
        trialObject.put("type",experimentType);
        trialObject.put("result",result);
        trialObject.put("user",user);
        trialObject.put("location",location);
        db.collection("trials")
                .add(trialObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        return this;
    }

    /*Get experiment of a trial*/
    String getExperimentReference(){ return this.experimentReference; }

    /*Get type of a trial*/
    String getExperimentType(){ return this.experimentType; }

    /*Get result of a trial*/
    String getResult(){ return this.result; }

    public String getUser() {
        return this.user;
    }

    public String getLocation() { return this.location; }
}