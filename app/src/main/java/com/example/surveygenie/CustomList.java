package com.example.surveygenie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Experiment> {

    private ArrayList<Experiment> experiments;
    private Context context;

    public CustomList(Context context, ArrayList<Experiment> experiments){
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        Experiment experiment = experiments.get(position);

        TextView description = view.findViewById(R.id.experiment_description);
        TextView regionName = view.findViewById(R.id.experiment_region);
        TextView trialNumber = view.findViewById(R.id.experiment_trial);
        TextView typeName = view.findViewById(R.id.experiment_type);

        description.setText(experiment.getDescription());
        regionName.setText(experiment.getRegionName());
        trialNumber.setText(experiment.getTrialNumber());
        typeName.setText(experiment.getTypeName());


        return view;

    }
}

