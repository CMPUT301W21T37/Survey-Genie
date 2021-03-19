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
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        final Button addExperimentButton = findViewById(R.id.add_experiment_button);

        addExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddExperiment().show(getSupportFragmentManager(), "ADD_EXPERIMENT");
            }
        });

        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tempDesp = experimentDataList.get(position).getDescription();
                Intent intent = new Intent(MainActivity.this,OptionsActivity.class);
                intent.putExtra("Description",tempDesp);
                startActivity(intent);

            }
        });

        experimentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Unpublish Experiment")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Unpublish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                experimentDataList.remove(position);
                                experimentAdapter.notifyDataSetChanged();
                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public void onOkPressed(Experiment newExperiment) { experimentAdapter.add(newExperiment); }

}