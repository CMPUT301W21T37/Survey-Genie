package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ExperimentActivity extends AppCompatActivity {
    private String eTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if Experiment.isOwner(User.id){
            setContentView(R.layout.activity_owner);
       // }else{
            setContentView(R.layout.activity_experimenter);
        //}
        Intent intent = getIntent();
        TextView experimentSelected = (TextView)findViewById(R.id.experimentName);
        eTitle = intent.getStringExtra("ExperimentTile");
        experimentSelected.setText(eTitle);

        //forum sett up
        final Button forumButton = findViewById(R.id.forum_button);
        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToForumActivity();
            }
        });
    }

    private void goToForumActivity(){
        Intent newintent = new Intent(this,ForumActivity.class);
        newintent.putExtra("ExperimentTitle",eTitle);
        startActivity(newintent);
    }

}
