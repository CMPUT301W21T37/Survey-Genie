package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Print a barchart about trials
Reference from https://www.youtube.com/watch?v=sXo2SkX7rGk
https://github.com/PhilJay/MPAndroidChart
 */
public class HistogramActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> trials;
    ArrayList<Float> results;
    ArrayList<Integer> quas;
    ArrayList<Trial> trialDataList;
    BarChart histogram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);

        Intent intent = getIntent();
        String ExperimentName = intent.getStringExtra("Experiment Name");
        Toast.makeText(HistogramActivity.this, ExperimentName, Toast.LENGTH_SHORT).show();

        trialDataList = new ArrayList<>();
        results = new ArrayList<>();
        quas = new ArrayList<>();

        histogram = (BarChart)findViewById(R.id.histogram);

        ArrayList<BarEntry>barEntries = new ArrayList<>();

        ArrayList<String>theTrials = new ArrayList<>();

        db.collection("trials")
                .whereEqualTo("Experiment", ExperimentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                trials = document.getData();
                                trialDataList.add(new Trial((String) trials.get("Experiment"), (String) trials.get("type"), (String) trials.get("result"), (String) trials.get("user"), (String) trials.get("location")));
                                results.add(Float.parseFloat((String) trials.get("result")));
                                quas.add(Integer.parseInt((String) trials.get("result")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            for (int i= 0; i < quas.size(); i ++){
                                String trial = "T" + String.valueOf(i+1);
                                int result = quas.get(i);
                                barEntries.add(new BarEntry(i,result));
                                theTrials.add(trial);
                            }
                            BarDataSet barDataSet = new BarDataSet(barEntries,"Trials");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            Description description = new Description();
                            description.setText("Results");
                            histogram.setDescription(description);

                            BarData theData = new BarData(barDataSet);
                            histogram.setData(theData);

                            XAxis xAxis = histogram.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(theTrials));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(theTrials.size());
                            xAxis.setLabelRotationAngle(0);

                            histogram.animateY(2000);
                            histogram.invalidate();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}
