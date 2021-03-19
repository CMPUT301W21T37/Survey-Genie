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

public class Experiment implements Serializable {
    private String description;
    private String region;
    private String trial;
    private String type;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Experiment(String description, String region, String trial, String type){
        this.description = description;
        this.region = region;
        this.trial = trial;
        this.type = type;
    }

    public Experiment uploadtodatabase(){
        Map<String,Object> experiment = new HashMap<>();
        experiment.put("Description", description);
        experiment.put("Region", region);
        experiment.put("Trial", trial);
        experiment.put("Experiment Type", type);
        db.collection("experiments")
                .add(experiment)
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

    String getDescription() { return this.description; }

    String getRegionName(){ return this.region; }

    String getTrialNumber() { return this.trial; }

    String getTypeName() { return this.type; }
}
