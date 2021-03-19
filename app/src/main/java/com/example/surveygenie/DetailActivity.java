package com.example.surveygenie;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Page for details of an experiment and option functions*/
public class DetailActivity extends AppCompatActivity {

    TextView description;
    TextView region;
    TextView trial;
    TextView type;
    ListView trialList;
    ArrayAdapter<Trial> trialAdapter;
    ArrayList<Trial> trialDataList;
    Map<String,Object> trials;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /*RadioGroup radioGroup;*/
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        description = (TextView) findViewById(R.id.experiment_description);
        region = (TextView) findViewById(R.id.experiment_region);
        trial = (TextView) findViewById(R.id.experiment_trial);
        type = (TextView) findViewById(R.id.experiment_type);

        Intent intent = getIntent();
        String tempDesp = intent.getStringExtra("Description");
        String tempRegion = intent.getStringExtra("Region");
        String tempTrial = intent.getStringExtra("Trial");
        String tempType = intent.getStringExtra("Type");

        description.setText(tempDesp);
        region.setText(tempRegion);
        trial.setText(tempTrial);
        type.setText(tempType);

        /*Show trials for a experiment*/
        trialList = (ListView)findViewById(R.id.trial_list);
        trialDataList = new ArrayList<>();
        trialAdapter = new TrialList(this,trialDataList);
        trialList.setAdapter(trialAdapter);
        db.collection("trials")
                .whereEqualTo("Experiment",tempDesp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                trials = document.getData();

                                trialDataList.add(new Trial((String) trials.get("Experiment"), (String) trials.get("type"), (String) trials.get("result") ));

                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                            trialAdapter = new TrialList(DetailActivity.this, trialDataList);
                            trialList.setAdapter(trialAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        /*Click to forum of questions for a experiment*/
        final Button forumButton = (Button) findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ForumActivity.class);
                intent.putExtra("Description", tempDesp);
                startActivity(intent);
            }
        });

        /*add new trial*/
        final Button addpostButton = (Button) findViewById(R.id.add_trial_button);
        addpostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder
                        .setView(view)
                        .setTitle("Add Trial"+ "\nType: "+tempType)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = view.findViewById(R.id.add_post_editText);
                                Trial trial = new Trial(tempDesp,tempType,edit.getText().toString());
                                trial.uploadtodatabase();
                                //other implementation will be added later
                                trialDataList.add(trial);
                                trialAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();

            }
        });
        //Statstics computation
        /*
        TextView quartiles = (TextView) findViewById(R.id.quartitles_text);
        TextView median = (TextView) findViewById(R.id.median_text);
        TextView mean = (TextView) findViewById(R.id.mean_text);
        TextView stdev = (TextView) findViewById(R.id.stdev_text);
        quartiles.setText("Q1value, Q3value");
        median.setText("Medianvalue");
        mean.setText("Meanvalue");
        stdev.setText("Stdevvalue");

         */
        //Statstics button set up
        final Button statsticsButton = (Button) findViewById(R.id.statistic_button);
        statsticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.statistics_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder
                        .setView(view)
                        .setTitle("Statstics")
                        .setNeutralButton("Ok",null)
                        .create();
                builder.show();
            }
        });

    }
}
