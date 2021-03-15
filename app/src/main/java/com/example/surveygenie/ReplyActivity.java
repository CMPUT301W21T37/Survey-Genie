package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity {
    private String qTitle;//question title
    ListView replyList;
    ArrayList<Reply> postedReplyList;
    ArrayAdapter<Reply> replyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        Intent intent = getIntent();
        TextView questionSelected = (TextView)findViewById(R.id.questiontTitle);
        qTitle = intent.getStringExtra("ExperimentTile");
        questionSelected.setText(qTitle);

        replyList = findViewById(R.id.reply_list);
        //Set Adapterview
        postedReplyList = new ArrayList<>() ;
        Question question = Question.getQuestion(qTitle);
        //replyAdapter= new PostList(this, postedReplyList);
        replyList.setAdapter(replyAdapter);//not set up

        //add reply
        Button addReplyButton = (Button)findViewById(R.id.add_r_button);

    }
}