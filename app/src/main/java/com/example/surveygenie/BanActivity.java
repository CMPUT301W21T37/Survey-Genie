package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FieldPath;


import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class BanActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> subscribers;
    ListView subscribersList;
    ArrayList<Subscriber> subscribersDataList;
    ArrayAdapter<Subscriber> subscribersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);

        Intent intent = getIntent();
        String ExperimentName = intent.getStringExtra("Experiment Name");

        subscribersList = findViewById(R.id.ban_user_list);
        subscribersDataList = new ArrayList<>();
        subscribersAdapter = new CustomBanList(BanActivity.this,subscribersDataList);
        subscribersList.setAdapter(subscribersAdapter);

        db.collection("subscribers")
                .whereEqualTo("Experiment",ExperimentName)
                .whereEqualTo("Status","Permitted")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscribers = document.getData();
                                subscribersDataList.add(new Subscriber((String)subscribers.get("Experiment"),(String) subscribers.get("Subscriber"),(String)subscribers.get("Status")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            subscribersAdapter = new CustomBanList(BanActivity.this,subscribersDataList);
                            subscribersList.setAdapter(subscribersAdapter);
                        }else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        subscribersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//                String subscriber = banDataList.get(position).getSubscriber();
//                db.collection("subscribers").document(subscriber)
//                        //.whereEqualTo("Subscriber",(String)subscriber)
//                        .update("Status", "Banned")
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully updated!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error updating document", e);
//                            }
//                        });
                new AlertDialog.Builder(BanActivity.this)
                        .setTitle("Ban this subscriber")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ban", new DialogInterface.OnClickListener() {
                            @Override
                            /*Delete in the firestore*/
                            public void onClick(DialogInterface dialog, int i) {
                                final String subscriber = subscribersDataList.get(position).getSubscriber();
                                db.collection("subscribers").document(subscriber)
                                        .update("Status", "Banned")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                                subscribersDataList.remove(position);
                                subscribersAdapter.notifyDataSetChanged();
                            }
                        }).show();
                return true;
            }
        });
    }
}