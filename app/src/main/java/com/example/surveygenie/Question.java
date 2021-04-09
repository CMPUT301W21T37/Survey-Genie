package com.example.surveygenie;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Question and its reply for a experiemnt*/
public class Question implements Serializable {
    private String question;
    private String experimentReference;
    private String user;
    private ArrayList<Reply> replys;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Question(String question,String experimentReference,String user) {
        this.question = question;
        this.experimentReference = experimentReference;
        this.replys = new ArrayList<Reply>();
        this.user = user;
    }

    /*Get replys of a question*/
    public ArrayList<Reply> getReplys() {
        return replys;
    }

    /*Add reply to the list of replys for a question*/
    public void addToReplys(Reply reply){
        this.replys.add(reply);
    }

    public String getQuestion() {
        return question;
    }

    public String getUser() {
        return user;
    }

    /*Upload data to the firestore*/
    public Question uploadtodatabase(){
        /*may need to implement user later*/
        Map<String,Object> quesitonObject = new HashMap<>();
        quesitonObject.put("Question",question);
        quesitonObject.put("Experiment",experimentReference);
        quesitonObject.put("User",user);
        db.collection("questions").document(question)
                .set(quesitonObject)
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


}
