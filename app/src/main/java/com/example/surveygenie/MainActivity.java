package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AddExperiment.OnFragmentInteractionListener{

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;
    SharedPreferences prefs;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        experimentList = findViewById(R.id.experiment_list);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);

        prefs = getSharedPreferences("username",MODE_PRIVATE);
        boolean idHasCreated = prefs.getBoolean("idCreated",false);
        if (idHasCreated == false){
            Intent intent = new Intent(MainActivity.this,CreateUserActivity.class);
            startActivity(intent);
        }

        final FloatingActionButton userProfile = findViewById(R.id.user_profile);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.getId();
                String userRole = user.getRole();

                Intent intent = new Intent(MainActivity.this,UserProfileActivity.class);
                intent.putExtra("Id",userId);
                intent.putExtra("Role",userRole);
                startActivity(intent);
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
    @Override
    public void onOkPressed(Experiment newExperiment) { experimentAdapter.add(newExperiment); }

}