package com.example.surveygenie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TrialList extends ArrayAdapter<Trial> {
    private ArrayList<Trial> trials;
    private Context context;

    public TrialList(Context context, ArrayList<Trial> trials) {
        super(context, 0, trials);
        this.trials = trials;
        this.context = context;
    }

    /*Fill in attributes*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.trial_content, parent, false);
        }


        Trial trial = trials.get(position);

        TextView result = view.findViewById(R.id.a_trial);
        String tr = String.valueOf(trial.getResult());

        result.setText(tr);

        return view;
    }
}

