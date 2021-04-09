package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DoubleValue;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Activity for calcute quarlities,median,mean and stdev
for experiment trials
 */

public class StatsticsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> trials;
    ArrayList<Float> results;
    ArrayList<Integer> quas;
    ArrayList<Trial> trialDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statstics);

        Intent intent = getIntent();
        String ExperimentName = intent.getStringExtra("Experiment Name");
        Toast.makeText(StatsticsActivity.this, ExperimentName, Toast.LENGTH_SHORT).show();
        trialDataList = new ArrayList<>();
        results = new ArrayList<>();
        quas = new ArrayList<>();

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
                            TextView quartile = (TextView) findViewById(R.id.quartitles_text);
                            TextView median = (TextView) findViewById(R.id.median_text);
                            TextView mean = (TextView) findViewById(R.id.mean_text);
                            TextView stdev = (TextView) findViewById(R.id.stdev_text);

                            quartile.setText(computeQuartile(quas));
                            median.setText(String.valueOf(computeMedian(results)));
                            mean.setText(String.valueOf(computeMean(results)));
                            stdev.setText(String.valueOf(computeStdev(results)));
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    /*Hints from
    https://www.khanacademy.org/math/statistics-probability/summarizing-quantitative-data/interquartile-range-iqr/a/interquartile-range-review
     */
    public String computeQuartile(ArrayList<Integer> quas){
        //Sort the array in increasing order
        quas.sort(Comparator.naturalOrder());

        int mid_index;

        //Check for even or odd case
        if (quas.size()%2 == 0){
            mid_index = quas.size()/2;
        }else{
            mid_index = (quas.size()-1)/2;
        }

        ArrayList<Float> left_half = new ArrayList<>();
        for (int i = 0; i < mid_index; i++){
            left_half.add((float)quas.get(i));
        }

        float Q1 = computeMedian(left_half);

        ArrayList<Float> right_half = new ArrayList<>();
        for (int i = mid_index; i < quas.size(); i++){
            right_half.add((float)quas.get(i));
        }

        float Q3 = computeMedian(right_half);


        return ("Q1:"+String.valueOf(Q1)+" Q3:"+String.valueOf(Q3));
    }


    /*Reference from geeksforgeeks
    https://www.geeksforgeeks.org/median/
    */

    public Float computeMedian(ArrayList<Float> results) {
        //Sort the array in increasing order
        results.sort(Comparator.naturalOrder());

        //Check for even case
        if (results.size() % 2 != 0) {
            return results.get(results.size() / 2);
        }
        Float before = results.get((results.size() - 1) / 2);
        Float after = results.get(results.size() / 2);
        return (before + after) / 2;
    }

    public Float computeMean(ArrayList<Float> results) {
        Float sum = Float.valueOf(0);
        for (int i = 0; i < results.size(); i++) {
            sum += results.get(i);
        }
        return (Float) sum / results.size();
    }

    /*References form geeksforgeeks
    https://www.geeksforgeeks.org/java-program-to-calculate-standard-deviation/
     */
    public Double computeStdev(ArrayList<Float> results) {
        Float mean = computeMean(results);
        Double std = Double.valueOf(0);
        for (int i = 0; i < results.size(); i++) {
            std += Math.pow(results.get(i) - mean, 2);
        }
        return Math.sqrt(std / results.size());
    }
}