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


import java.util.ArrayList;
import java.util.UUID;

import static java.sql.DriverManager.println;


public class MainActivity extends AppCompatActivity implements AddExperiment.OnFragmentInteractionListener{

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    ArrayList<Experiment> experimentDataList;
    SharedPreferences preference;
    ArrayList<User> owners;
    ArrayList<User> experimenters;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //retrive username if previously saved on this mobile device.
        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userid= preference.getString("UserID",null);
        user = new User(userid);
        //if username don't exist , go to welcome page for a new user.
        if (userid == null){
            setContentView(R.layout.create_user);//go to layout for setting up user
            SharedPreferences.Editor editor = preference.edit();
            String uID = UUID.randomUUID().toString();//give user a random unique id
            editor.putString("UserID",uID );//load this unique id into preferences
            editor.commit();//save
            user = new User(uID);//create a user with this unique uid
            //click on experimenter
            final Button asExperimenter = findViewById(R.id.as_experimenter);
            asExperimenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    experimenters.add(user);
                }
            });
            //click on owner
            final Button asOwner =findViewById(R.id.as_owner);
            asOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    owners.add(user);
                }
            });
        }

        setContentView(R.layout.activity_main);//initialize mainactivity

        experimentList = findViewById(R.id.experiment_list);
        //connect to db , owner connected owned experiment, experimenter connec to subscribed experiment
        experimentDataList = new ArrayList<>();

        experimentAdapter = new CustomList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);

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

                setContentView(R.layout.options_layout);
                String tempDesp = experimentDataList.get(position).getDescription();
                Intent intent = new Intent(MainActivity.this,ForumActivity.class);
                intent.putExtra("Description",tempDesp);
                startActivity(intent);

                /*
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

                 */
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
    //get username generated in CreateUserAcitivity

}