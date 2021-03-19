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

public class User implements Serializable {
    private String id;
    private String contact;
    private String role;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    User(String id,String contact){
        this.id = id;
        this.contact = contact;
    }

    public User upLoadToDatabase(){
        Map<String,Object> users = new HashMap<>();
        users.put("UserId", id);
        users.put("UserContactInfo", contact);
        db.collection("users")
                .add(users)
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

    void setRole(String role){
        this.role = role;
    }

    void setContact(String contact){
        this.contact = contact;
    }

    String getId(){ return this.id; }

    String getContact(){ return this.contact; }
}
