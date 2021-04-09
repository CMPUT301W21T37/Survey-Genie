package com.example.surveygenie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.content.Context.MODE_PRIVATE;

/*Use DialogFragment to add a new experiment*/
public class AddExperiment extends DialogFragment{
    private EditText experimentDescription;
    private EditText experimentRegion;
    private EditText experimentTrial;
    private EditText experimentName;
    private String experimentType;
    RadioGroup radioGroup;
    private OnFragmentInteractionListener listener;
    SharedPreferences preference;

    /*Show a interface about add a new experiment*/
    public interface OnFragmentInteractionListener {
        void onOkPressed(Experiment newExperiment);

        void onResume(Bundle savedInstanceState);
    }


    static AddExperiment newInstance(Experiment experiment) {
        Bundle args = new Bundle();
        args.putSerializable("experiment", experiment);

        AddExperiment fragment = new AddExperiment();
        fragment.setArguments(args);
        return fragment;
    }

    /*Check implement error*/
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

    /*Set informations to add a new experiment*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_experiment, null);

        //get current username
        experimentName = view.findViewById(R.id.add_experiment_name);
        experimentDescription = view.findViewById(R.id.add_experiment_description);
        experimentRegion = view.findViewById(R.id.add_experiment_region);
        experimentTrial = view.findViewById(R.id.add_experiment_trial);

        /*Set and get type of a new experiment*/
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
                preference = getActivity().getSharedPreferences("username",MODE_PRIVATE);
                String userId= preference.getString("userID",null);
                String name = experimentName.getText().toString();
                String description = experimentDescription.getText().toString();
                String region = experimentRegion.getText().toString();
                String trial = experimentTrial.getText().toString();
                Experiment expt = new Experiment(name,description, region, trial, experimentType,userId,"Open","Off");
                listener.onOkPressed(expt.uploadtodatabase());
            }
        });
        return builder.create();

    }
}