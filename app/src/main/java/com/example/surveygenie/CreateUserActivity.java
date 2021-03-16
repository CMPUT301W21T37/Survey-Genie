package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateUserActivity extends AppCompatActivity {
    public ArrayList<User> owners;
    public ArrayList<User> experiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Button selected_Experimenter = (Button) findViewById(R.id.selcet_experimenter_button);
        selected_Experimenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new User(generateId()) experimenter;
                //experimenters.add(experimenter);
            }
        });

        Button selected_Owner = (Button) findViewById(R.id.select_owner_button);
        selected_Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new User(generateId()) owner;
                //owners.add(owner);
            }
        });
    }

    //private String generateId(){}



}