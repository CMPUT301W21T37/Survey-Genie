package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Main page of the app and its functions*/
public class MainActivity extends AppCompatActivity implements AddExperiment.OnFragmentInteractionListener{

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    public ArrayList<Experiment> experimentDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> experiment;
    FloatingActionButton userProfile;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experimentList = findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);
        userProfile = findViewById(R.id.user_profile);
        prefs = getSharedPreferences("username", MODE_PRIVATE);
        boolean idHasCreated = prefs.getBoolean("idCreated", false);
        if (idHasCreated == false) {
            Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
            startActivity(intent);
        }

        /*Get all experiments in firestore*/
        db.collection("experiments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                experiment = document.getData();
                                experimentDataList.add(new Experiment((String)experiment.get("Description"), (String)experiment.get("Region"), (String)experiment.get("Trial"), (String)experiment.get("Experiment Type")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                            experimentList.setAdapter(experimentAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

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
        String role = prefs.getString("userRole",null);

        final Button addExperimentButton = findViewById(R.id.add_experiment_button);

        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (role.equals("Owner")) {
                    new AddExperiment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
                }else if (role.equals("Experimenter")) {
                    final String warning = "As an experimenter, you can't publish experiment!";
                    Toast.makeText(MainActivity.this, warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

        final CollectionReference ref = db.collection("experiments");

        /*Click to see experiment details and option function*/
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tempDesp = experimentDataList.get(position).getDescription();
                String tempRegion = experimentDataList.get(position).getRegionName();
                String tempTrial = experimentDataList.get(position).getTrialNumber();
                String tempType = experimentDataList.get(position).getTypeName();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("Description",tempDesp);
                intent.putExtra("Region",tempRegion);
                intent.putExtra("Trial",tempTrial);
                intent.putExtra("Type",tempType);
                startActivity(intent);

            }
        });

        /*Unpublish an experiment*/
        experimentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (role.equals("Owner")) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Unpublish Experiment")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Unpublish", new DialogInterface.OnClickListener() {
                                @Override
                                /*Delete in the firestore*/
                                public void onClick(DialogInterface dialog, int i) {
                                    final String experimentDesp = experimentDataList.get(position).getDescription();
                                    db.collection("experiments").document(experimentDesp)
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
                                    /*experimentDataList.remove(position);
                                    experimentAdapter.notifyDataSetChanged();*/
                                }
                            }).show();
                } else if (role.equals("Experimenter")) {
                    final String warning = "As an experimenter, you can't unpublish experiment!";
                    Toast.makeText(MainActivity.this, warning, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    /*Confirm to add a new experiment*/
    @Override
    public void onOkPressed(Experiment newExperiment) { experimentAdapter.add(newExperiment); }

}