package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OptionsActivity extends AppCompatActivity {
    String experimentDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent intent = getIntent();
        experimentDescription = intent.getStringExtra("Description");
        TextView optionTitle = (TextView) findViewById(R.id.option_title);
        optionTitle.setText(experimentDescription);

        //use visibility to set different button to owner and experimenter



        //implement all button function
        final Button forumButton = (Button) findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsActivity.this,ForumActivity.class);
                intent.putExtra("Description",experimentDescription);
                startActivity(intent);
            }
        });


    }
}