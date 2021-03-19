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

/*Get attributes of an experiment and upload the data to the firestore*/
public class Experiment implements Serializable {
    private String description;
    private String region;
    private String trial;
    private String type;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /*Set an experiment with attributes*/
    Experiment(String description, String region, String trial, String type){
        this.description = description;
        this.region = region;
        this.trial = trial;
        this.type = type;
    }

    /*Upload data of experiments to the firestore*/
    public Experiment uploadtodatabase(){
        Map<String,Object> experiment = new HashMap<>();
        experiment.put("Description", description);
        experiment.put("Region", region);
        experiment.put("Trial", trial);
        experiment.put("Experiment Type", type);
        db.collection("experiments").document(description)
                .set(experiment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        return this;
    }

    /*Get description of an experiment*/
    String getDescription() { return this.description; }

    /*Get region of an experiment*/
    String getRegionName(){ return this.region; }

    /*Get minimum trials of an experiment*/
    String getTrialNumber() { return this.trial; }

    /*Get type of an experiment*/
    String getTypeName() { return this.type; }
}
