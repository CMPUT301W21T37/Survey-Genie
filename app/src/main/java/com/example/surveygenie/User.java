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

/*Information for a user*/
public class User implements Serializable {
    private String id;
    private String role;
    private String contact;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /*Fill in attributes for a user*/
    User(String id, String role, String contact){
        this.id = id;
        this.role = role;
        this.contact = contact;
    }

    /*Upload data for the user to the forestore*/
    public User upLoadToDatabase(){
        Map<String,Object> users = new HashMap<>();
        users.put("UserId", id);
        users.put("UserRole", role);
        users.put("UserContactInfo", contact);
        db.collection("users").document(id)
                .set(users)
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

    /*Get id of a user*/
    String getId(){ return this.id; }

    /*Get role of a user*/
    String getRole(){ return this.role; }

    /*Get contact information of a user*/
    String getContact(){ return this.contact; }
}
