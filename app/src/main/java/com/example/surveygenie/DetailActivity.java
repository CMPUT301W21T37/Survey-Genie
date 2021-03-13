package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    TextView description;
    TextView region;
    TextView trial;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        description = (TextView)findViewById(R.id.experiment_description);
        region = (TextView) findViewById(R.id.experiment_region);
        trial = (TextView) findViewById(R.id.experiment_trial);

        Intent intent = getIntent();
        String tempDesp = intent.getStringExtra("Description");
        String tempRegion = intent.getStringExtra("Region");
        String tempTrial = intent.getStringExtra("Trial");

        description.setText(tempDesp);
        region.setText(tempRegion);
        trial.setText(tempTrial);

        radioGroup = (RadioGroup)findViewById(R.id.radio_Group);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
            }
        });

        final Button saveButton = findViewById(R.id.save_experiment_button);
    }

}
