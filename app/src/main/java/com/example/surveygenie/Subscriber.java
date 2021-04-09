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

public class Subscriber implements Serializable {
    private String experimentReference;
    private String subscriber;
    private String status;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Subscriber(String experimentReference, String subscriber,String status) {
        this.experimentReference = experimentReference;
        this.subscriber = subscriber;
        this.status = status;
    }

    public Subscriber uploadtodatabase(){
        Map<String,Object> subscriberObject = new HashMap<>();
        subscriberObject.put("Experiment",experimentReference);
        subscriberObject.put("Subscriber",subscriber);
        subscriberObject.put("Status", status);
        db.collection("subscribers")
                .add(subscriberObject)
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

    public String getSubscriber() {
        return subscriber;
    }

    public String getStatus(){return status;}

    public String getExperimentReference() {
        return experimentReference;
    }
}
