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

/*Reply and which question it belongs to*/
public class Reply implements Serializable {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String reply;
    private String questionReference;
    private String user;

    public Reply(String reply,String questionReference,String user) {
        this.reply = reply;
        this.questionReference = questionReference;
        this.user = user;
    }

    /*Upload data to the firestore*/
    public Reply uploadtodatabase(){
        Map<String,Object> replyObject = new HashMap<>();
        replyObject.put("Reply",reply);
        replyObject.put("Question",questionReference);
        replyObject.put("User",user);
        db.collection("replys").document(reply)
                .set(replyObject)
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

    public String getReply() {
        return reply;
    }

    public String getUser() {
        return user;
    }
}
