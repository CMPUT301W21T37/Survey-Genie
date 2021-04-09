package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {

    ListView experimentList;
    ArrayAdapter<Experiment> experimentAdapter;
    public ArrayList<Experiment> experimentDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //load data from db
        experimentList = findViewById(R.id.search_experiment_list);
        experimentDataList = new ArrayList<>();
        experimentAdapter = new CustomSearchList(this, experimentDataList);
        experimentList.setAdapter(experimentAdapter);

        db.collection("experiments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                experiment = document.getData();
                                experimentDataList.add(new Experiment((String)experiment.get("Name"),(String)experiment.get("Description"), (String)experiment.get("Region"), (String)experiment.get("Trial"), (String)experiment.get("Experiment Type"),(String)experiment.get("Owner"),(String)experiment.get("Status"),(String)experiment.get("Location")));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            experimentAdapter = new CustomSearchList(SearchActivity.this, experimentDataList);
                            experimentList.setAdapter(experimentAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<Experiment> filteredList = new ArrayList<>();
        for (Experiment item : experimentDataList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())||item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item);
            }

        }
        
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            experimentAdapter = new CustomSearchList(SearchActivity.this, filteredList);
            experimentList.setAdapter(experimentAdapter);
            experimentAdapter.notifyDataSetChanged();
        }
    }
}