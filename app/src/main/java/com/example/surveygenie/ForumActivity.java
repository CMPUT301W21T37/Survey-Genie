package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Collections;

public class ForumActivity extends AppCompatActivity implements AddQuestion.OnInteractionLisnter{
    private String eTitle;
    ListView postList;
    ArrayList<Question> postedQuestionList;
    ArrayAdapter<Question> questionAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        TextView experimentSelected = (TextView)findViewById(R.id.experimentTitle);
        eTitle = intent.getStringExtra("ExperimentTile");
        experimentSelected.setText(eTitle);


        //Set Adapterview
        postList = findViewById(R.id.question_list);
        postedQuestionList = new ArrayList<>() ;
        Experiment experiment = Experiment.getExperiment(eTitle);
        questionAdapter= new PostList(this, postedQuestionList);
        postList.setAdapter(questionAdapter);//not set up

        //ADD question
        Button addQuestionButton = (Button)findViewById(R.id.add_q_button);//add question
        if (experiment.isOwner(user.id)){//if owner can't add
            addQuestionButton.setVisibility(View.GONE);
        }
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddQuestion().show(getSupportFragmentManager(),"ADD Question");
            }
        }

    }
    public void onOkPressed(Question newQuestion) { questionAdapter.add(newQuestion); }
}