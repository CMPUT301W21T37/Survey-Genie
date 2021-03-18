package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        experimentDataList = new ArrayList<>();
        experimentList = findViewById(R.id.experiment_list);
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
                String tempRegion = experimentDataList.get(position).getRegionName();
                String tempTrial = experimentDataList.get(position).getTrialNumber();
                String tempType = experimentDataList.get(position).getTypeName();

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("Description",tempDesp);
                intent.putExtra("Region",tempRegion);
                intent.putExtra("Trial",tempTrial);
                intent.putExtra("Type",tempType);
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

/*    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        experimentDataList = new ArrayList<>();
        experimentList = findViewById(R.id.experiment_list);
        db.collection("experiments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        for(QueryDocumentSnapshot document: value) {
                            experiment = document.getData();
                            experimentDataList.add(new Experiment((String)experiment.get("Description"), (String)experiment.get("Region"), (String)experiment.get("Trial"), (String)experiment.get("Experiment Type")));
                        }
                        experimentAdapter = new CustomList(MainActivity.this, experimentDataList);
                        experimentList.setAdapter(experimentAdapter);
                    }
                });
    }*/

    @Override
    public void onOkPressed(Experiment newExperiment) { experimentAdapter.add(newExperiment); }

}