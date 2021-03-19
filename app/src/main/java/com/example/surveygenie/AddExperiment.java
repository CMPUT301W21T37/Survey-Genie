package com.example.surveygenie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class AddExperiment extends DialogFragment{
    private EditText experimentDescription;
    private EditText experimentRegion;
    private EditText experimentTrial;
    private String experimentType;
    RadioGroup radioGroup;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);
    }


    static AddExperiment newInstance(Experiment experiment) {
        Bundle args = new Bundle();
        args.putSerializable("experiment", experiment);

        AddExperiment fragment = new AddExperiment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_experiment, null);

        experimentDescription = view.findViewById(R.id.add_experiment_description);
        experimentRegion = view.findViewById(R.id.add_experiment_region);
        experimentTrial = view.findViewById(R.id.add_experiment_trial);

        radioGroup = (RadioGroup)view.findViewById(R.id.radio_Group);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                experimentType = radioButton.getText().toString();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Add Experiment");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String description = experimentDescription.getText().toString();
                String region = experimentRegion.getText().toString();
                String trial = experimentTrial.getText().toString();
                Experiment expt = new Experiment(description, region, trial, experimentType);
                listener.onOkPressed(expt.uploadtodatabase());
            }
        });
        return builder.create();

    }
}