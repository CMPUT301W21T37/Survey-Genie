package com.example.surveygenie;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Main page of the app and its functions*/
public class MainActivity extends AppCompatActivity implements AddExperiment.OnFragmentInteractionListener, LifecycleObserver {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    public ArrayList<Experiment> experimentDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> experiment;
    Map<String,Object> subscribers;
    FloatingActionButton userProfile;
    SharedPreferences prefs;
    ArrayList<String> subscribedExperiments;
    ComponentName componentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        componentName = activityManager.getRunningTasks(1).get(0).topActivity;

        experimentList = findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);
        userProfile = findViewById(R.id.user_profile);
        prefs = getSharedPreferences("username", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //default role
        boolean idHasCreated = prefs.getBoolean("idCreated", false);
        if (idHasCreated == false){
            editor.putString("userRole","null");
            editor.putString("userID","null");
        }
        editor.commit();



        String role = prefs.getString("userRole",null);
        if (role.equals("null")&&idHasCreated == false) {
            Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
            startActivityForResult(intent,1);

        }

        String userId= prefs.getString("userID",null);
        subscribedExperiments =new ArrayList<>();
        /*Get all experiments in firestore*/
        db.collection("subscribers")
                .whereEqualTo("Subscriber",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscribers = document.getData();
                                subscribedExperiments.add((String) subscribers.get("Experiment"));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            db.collection("experiments")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()){
                                                    experiment = document.getData();
                                                    if(subscribedExperiments.contains((String)experiment.get("Name"))){
                                                        experimentDataList.add(new Experiment((String)experiment.get("Name"),(String)experiment.get("Description"), (String)experiment.get("Region"), (String)experiment.get("Trial"), (String)experiment.get("Experiment Type"),(String)experiment.get("Owner"),(String)experiment.get("Status"),(String)experiment.get("Location")));
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                    }
                                                }
                                                experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                                                experimentList.setAdapter(experimentAdapter);
                                            }
                                        }
                                    });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        if(role.equals("Owner")) {
            db.collection("experiments")
                    .whereEqualTo("Owner", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    experiment = document.getData();
                                    experimentDataList.add(new Experiment((String) experiment.get("Name"), (String) experiment.get("Description"), (String) experiment.get("Region"), (String) experiment.get("Trial"), (String) experiment.get("Experiment Type"), (String) experiment.get("Owner"), (String) experiment.get("Status"), (String) experiment.get("Location")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                                experimentList.setAdapter(experimentAdapter);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        /*Click to user profile page*/
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.user_profile:
                        intent.setClass(v.getContext(), UserProfileActivity.class);
                        v.getContext().startActivity(intent);
                        break;
                }
            }
        });

        /*Show adding a new experiment window*/
        /*View addExperiment = findViewById(R.id.add_experiment_button);
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String role = pref.getString("userRole",null);
        if (role.equals("Experimenter")){
            addExperiment.setVisibility(View.GONE);
        }*/


        final Button addExperimentButton = findViewById(R.id.add_experiment_button);

        if(role.equals("Experimenter")){
            addExperimentButton.setVisibility(View.GONE);
        }
        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("Owner")) {
                    new AddExperiment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
                }
            }
        });

        final CollectionReference ref = db.collection("experiments");

        /*Click to see experiment details and option function*/
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tempName = experimentDataList.get(position).getName();
                String tempDesp = experimentDataList.get(position).getDescription();
                String tempRegion = experimentDataList.get(position).getRegionName();
                String tempTrial = experimentDataList.get(position).getTrialNumber();
                String tempType = experimentDataList.get(position).getTypeName();
                String tempOwner = experimentDataList.get(position).getOwner();
                String tempStatus = experimentDataList.get(position).getStatus();
                String tempLoc = experimentDataList.get(position).getLocation();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("Name",tempName);
                intent.putExtra("Description",tempDesp);
                intent.putExtra("Region",tempRegion);
                intent.putExtra("Trial",tempTrial);
                intent.putExtra("Type",tempType);
                intent.putExtra("Owner",tempOwner);
                intent.putExtra("Status",tempStatus);
                intent.putExtra("Location",tempLoc);
                startActivity(intent);
            }
        });
        //search function
        ImageView searchClick = (ImageView)findViewById(R.id.search_click);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);

            }
        });


        /*Unpublish an experiment*/
        experimentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String experimentOwner = experimentDataList.get(position).getOwner();
                String userId = prefs.getString("userID",null);
                String role = prefs.getString("userRole",null);

                if (experimentOwner.equals(userId)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Unpublish Experiment")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Unpublish", new DialogInterface.OnClickListener() {
                                @Override
                                /*Delete in the firestore*/
                                public void onClick(DialogInterface dialog, int i) {
                                    final String experimentName = experimentDataList.get(position).getName();
                                    db.collection("experiments").document(experimentName)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                }
                                            });
                                    experimentDataList.remove(position);
                                    experimentAdapter.notifyDataSetChanged();
                                }
                            }).show();
                } else if (role.equals("Experimenter")) {
                    final String warning = "As an experimenter, you can't unpublish an experiment!";
                    Toast.makeText(MainActivity.this, warning, Toast.LENGTH_SHORT).show();
                }else if (role.equals("Owner") && !experimentOwner.equals(userId)){
                    final String warning2 = "You aren't the owner of this experiment, you can't unpublish it!";
                    Toast.makeText(MainActivity.this, warning2, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    /*Confirm to add a new experiment*/
    @Override
    public void onOkPressed(Experiment newExperiment) { experimentAdapter.add(newExperiment); }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("Experiment Description");

            }
        }
    }
    @Override
    public void onResume(Bundle savedInstanceState){
        reloadDb();
    }
    public void reloadDb(){
        prefs = getSharedPreferences("username", MODE_PRIVATE);
        String userId= prefs.getString("userID",null);
        subscribedExperiments =new ArrayList<>();
        String role = prefs.getString("userRole",null);

        db.collection("subscribers")
                .whereEqualTo("Subscriber",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscribers = document.getData();
                                subscribedExperiments.add((String) subscribers.get("Experiment"));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            db.collection("experiments")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()){
                                                    experiment = document.getData();
                                                    if(subscribedExperiments.contains((String)experiment.get("Name"))){
                                                        experimentDataList.add(new Experiment((String)experiment.get("Name"),(String)experiment.get("Description"), (String)experiment.get("Region"), (String)experiment.get("Trial"), (String)experiment.get("Experiment Type"),(String)experiment.get("Owner"),(String)experiment.get("Status"),(String)experiment.get("Location")));
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                    }
                                                }
                                                experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                                                experimentList.setAdapter(experimentAdapter);
                                            }
                                        }
                                    });
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        if(role.equals("Owner")) {
            db.collection("experiments")
                    .whereEqualTo("Owner", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    experiment = document.getData();
                                    experimentDataList.add(new Experiment((String) experiment.get("Name"), (String) experiment.get("Description"), (String) experiment.get("Region"), (String) experiment.get("Trial"), (String) experiment.get("Experiment Type"), (String) experiment.get("Owner"), (String) experiment.get("Status"), (String) experiment.get("Location")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                                experimentList.setAdapter(experimentAdapter);
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

}