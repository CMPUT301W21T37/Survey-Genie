package com.example.surveygenie;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.components.Description;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    Map<String,Object> subscribers;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /*RadioGroup radioGroup;*/
    FloatingActionButton getLocation;
    FloatingActionButton scanTrial;
    SharedPreferences preference;
    String subscriberStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        description = (TextView) findViewById(R.id.experiment_description);
        region = (TextView) findViewById(R.id.experiment_region);
        trial = (TextView) findViewById(R.id.experiment_trial);
        type = (TextView) findViewById(R.id.experiment_type);

        Intent intent = getIntent();
        String tempName = intent.getStringExtra("Name");
        String tempDesp = intent.getStringExtra("Description");
        String tempRegion = intent.getStringExtra("Region");
        String tempTrial = intent.getStringExtra("Trial");
        String tempType = intent.getStringExtra("Type");
        String tempOwner = intent.getStringExtra("Owner");
        String tempStatus = intent.getStringExtra("Status");
        String tempLoc = intent.getStringExtra("Location");

        preference = getSharedPreferences("username",MODE_PRIVATE);
        String role = preference.getString("userRole",null);

        if (tempLoc.equals("On") && role.equals("Experimenter")){
            final String warning = "Please add geo-location while adding trials!!!";
            Toast.makeText(DetailActivity.this, warning, Toast.LENGTH_SHORT).show();
        }

        description.setText(tempDesp);
        region.setText(tempRegion);
        trial.setText(tempTrial);
        type.setText(tempType);

        /*Show trials for a experiment*/
        trialList = (ListView)findViewById(R.id.trial_list);
        trialDataList = new ArrayList<>();
        trialAdapter = new TrialList(this,trialDataList);
        trialList.setAdapter(trialAdapter);

        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("userID",null);
        db.collection("subscribers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                subscribers = document.getData();
                                if (subscribers.get("Experiment").equals(tempName)&&subscribers.get("Subscriber").equals(userId)) {
                                    subscriberStatus=(String) subscribers.get("Status");
                                }
                            }
                            if(!subscriberStatus.equals("Banned")){
                                db.collection("trials")
                                        .whereEqualTo("Experiment",tempName)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()){
                                                        trials = document.getData();

                                                        trialDataList.add(new Trial((String) trials.get("Experiment"), (String) trials.get("type"), (String) trials.get("result"),(String) trials.get("user"),(String)trials.get("location") ));

                                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                                    }
                                                    trialAdapter = new TrialList(DetailActivity.this, trialDataList);
                                                    trialList.setAdapter(trialAdapter);
                                                } else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                            }
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
                intent.putExtra("Experiment Name", tempName);
                startActivity(intent);
            }
        });

        /*Click to scan QR code*/
        View scan = findViewById(R.id.scan_button);
        if (role.equals("Owner")){
            scan.setVisibility(View.GONE);
        }

        scanTrial = findViewById(R.id.scan_button);
        scanTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,ScanQRCodeActivity.class);
                startActivity(intent);
            }
        });


        /*Click to get location page*/
        View loc = findViewById(R.id.get_location);
        if (role.equals("Owner")){
            loc.setVisibility(View.GONE);
        }else if (tempLoc.equals("Off")){
            loc.setVisibility(View.GONE);
        }

        getLocation = findViewById(R.id.get_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, GetLocationActivity.class);
                String userId = preference.getString("userID",null);
                intent.putExtra("User ID", userId);
                startActivity(intent);
            }
        });

        /*add new trial*/
        View addTrial = findViewById(R.id.add_trial_button);
        if (role.equals("Owner")){
            addTrial.setVisibility(View.GONE);
        }

        final Button addpostButton = (Button) findViewById(R.id.add_trial_button);
        addpostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("subscribers")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        subscribers = document.getData();
                                        if (subscribers.get("Experiment").equals(tempName)&&subscribers.get("Subscriber").equals(userId)) {
                                            subscriberStatus=(String) subscribers.get("Status");
                                        }
                                    }
                                    String role = preference.getString("userRole",null);
                                    if(tempStatus.equals("Open") && role.equals("Experimenter")&&subscriberStatus.equals("Permitted")) {
                                        final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                        builder
                                                .setView(view)
                                                .setTitle("Add Trial" + "\nType: " + tempType)
                                                .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        EditText edit = view.findViewById(R.id.add_post_editText);
                                                        Trial trial = new Trial(tempName, tempType, edit.getText().toString(), userId, null);
                                                        trial.uploadtodatabase();
                                                        //other implementation will be added later
                                                        trialDataList.add(trial);
                                                        trialAdapter.notifyDataSetChanged();
                                                    }
                                                }).create();
                                        builder.show();
                                    }else if(tempStatus.equals("Closed")){
                                        final String notification2="Experiment already Ended";
                                        Toast.makeText(DetailActivity.this,notification2, Toast.LENGTH_SHORT).show();
                                    }else if(subscriberStatus.equals("Banned")){
                                        final String notification3="You are banned from entering result";
                                        Toast.makeText(DetailActivity.this,notification3, Toast.LENGTH_SHORT).show();
                                    }
                /*else if (role.equals("Owner")){
                    addpostButton.setVisibility(View.INVISIBLE);
                    final String notification="As an owner, you can't add trials.";
                    Toast.makeText(DetailActivity.this,notification, Toast.LENGTH_SHORT).show();
                }*/
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
        });

        //Statstics button set up
        final Button statsticsButton = (Button) findViewById(R.id.statistic_button);
        statsticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trialDataList.size()>= Integer.parseInt(tempTrial)){
                    Intent intent = new Intent(DetailActivity.this, StatsticsActivity.class);
                    intent.putExtra("Experiment Name", tempName);
                    startActivity(intent);
                }else{
                    final String warning="insufficient trials for statstics";
                    Toast.makeText(DetailActivity.this,warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button histogramButton = (Button) findViewById(R.id.histogram_button);
        histogramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trialDataList.size()>= 1){
                    Intent intent = new Intent(DetailActivity.this, HistogramActivity.class);
                    intent.putExtra("Experiment Name", tempName);
                    startActivity(intent);
                }else{
                    final String warning="insufficient trials for histogram";
                    Toast.makeText(DetailActivity.this,warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button plotButton = (Button) findViewById(R.id.plot_button);
        plotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trialDataList.size()>= 1){
                    Intent intent = new Intent(DetailActivity.this, PlotActivity.class);
                    intent.putExtra("Experiment Name", tempName);
                    startActivity(intent);
                }else{
                    final String warning="insufficient trials for plot";
                    Toast.makeText(DetailActivity.this,warning, Toast.LENGTH_SHORT).show();
                }
            }
        });


        //End experiment
        View end = findViewById(R.id.end_button);
        if (role.equals("Experimenter")){
            end.setVisibility(View.GONE);
        }

        final Button endButton = (Button) findViewById(R.id.end_button);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = preference.getString("userRole",null);
                String userId = preference.getString("userID",null);

                if (tempOwner.equals(userId)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder
                            .setTitle("End Experiment")
                            .setMessage("Are you sure you want to end experiment")
                            .setNegativeButton("Cancel",null)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("experiments").document(tempName)
                                            .update("Status", "Closed")
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
                                    finish();
                                }
                            }).create();
                    builder.show();
                }/*else if (role.equals("Experimenter")){
                    endButton.setVisibility(View.GONE);
                    final String warning = "As an experimenter, you can't end this experiment!";
                    Toast.makeText(DetailActivity.this, warning, Toast.LENGTH_SHORT).show();
                }*/else if (role.equals("Owner") && !tempOwner.equals(userId)){
                    final String warning = "You aren't the owner of this experiment, you can't end it!";
                    Toast.makeText(DetailActivity.this, warning, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Set geo-location required for trials
        View geoLoc = findViewById(R.id.location_button);
        if (role.equals("Experimenter")){
            geoLoc.setVisibility(View.GONE);
        }

        final Button locButton = (Button) findViewById(R.id.location_button);
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = preference.getString("userRole",null);
                String userId = preference.getString("userID",null);

                if (tempOwner.equals(userId)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder
                            .setTitle("Set geo-location requirements for trials")
                            .setMessage("Are you sure you want to set trials needed geo-location")
                            .setNegativeButton("Cancel",null)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.collection("experiments").document(tempName)
                                            .update("Location", "On")
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
                                }
                            }).create();
                    builder.show();
                }else if (role.equals("Owner") && !tempOwner.equals(userId)){
                    final String warning = "You aren't the owner of this experiment, you can't change location requirement!";
                    Toast.makeText(DetailActivity.this, warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Generate QR code
        trialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = trialDataList.get(position).getResult();
                Intent intent = new Intent(DetailActivity.this,QRGenerationActivity.class);
                intent.putExtra("Result",result);
                intent.putExtra("Experiment Name",tempName);
                intent.putExtra("Experiment Type",tempType);
                startActivity(intent);
            }
        });
        //unsub
        final  Button unsubscribe = (Button) findViewById(R.id.unsub_button);
        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("experiments").document((userId+tempName))
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
                Intent intent= new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
