package com.example.surveygenie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Print a linechart about trials
Reference from https://www.youtube.com/watch?v=DD1CxoVONFE&t=116s
https://github.com/PhilJay/MPAndroidChart
 */
public class PlotActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> trials;
    ArrayList<Float> results;
    ArrayList<Integer> quas;
    ArrayList<Trial> trialDataList;
    LineChart plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        Intent intent = getIntent();
        String ExperimentName = intent.getStringExtra("Experiment Name");
        Toast.makeText(PlotActivity.this, ExperimentName, Toast.LENGTH_SHORT).show();

        trialDataList = new ArrayList<>();
        results = new ArrayList<>();
        quas = new ArrayList<>();

        plot = (LineChart)findViewById(R.id.plot);

        /*
        plot.setDragEnabled(true);
        plot.setScaleEnabled(true);*/

        ArrayList<Entry>yValues = new ArrayList<>();

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
                                yValues.add(new BarEntry(i,result));
                                theTrials.add(trial);
                            }
                            LineDataSet lineDataSet = new LineDataSet(yValues,"Trials");

                            Description description = new Description();
                            description.setText("Results");
                            plot.setDescription(description);

                            lineDataSet.setFillAlpha(110);

                            lineDataSet.setLineWidth(3f);
                            lineDataSet.setValueTextSize(10f);
                            lineDataSet.setValueTextColor(Color.BLACK);

                            LineData theData = new LineData(lineDataSet);

                            plot.setData(theData);

                            XAxis xAxis = plot.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(theTrials));
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);
                            xAxis.setDrawAxisLine(false);
                            xAxis.setGranularity(1f);
                            xAxis.setLabelCount(theTrials.size());
                            xAxis.setLabelRotationAngle(0);

                            plot.animateY(2000);
                            plot.invalidate();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }
}
