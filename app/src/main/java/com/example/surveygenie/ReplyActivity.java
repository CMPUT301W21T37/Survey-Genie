package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity implements AddReply.OnInteractionLisnter {
    private String qTitle;//question title
    ListView replyList;
    ArrayList<Reply> postedReplyList;
    ArrayAdapter<Reply> replyAdapter;
    Question question;
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
        question = Question.getQuestion(qTitle);
        postedReplyList = question.getReplys();//get replys that question holds
        //replyAdapter= new ReplyList(this, postedReplyList);
        replyAdapter = new ArrayAdapter<Reply> (this, R.layout.reply_layout,postedReplyList);
        replyList.setAdapter(replyAdapter);//not set up

        //add reply
        Button addReplyButton = (Button)findViewById(R.id.add_r_button);
        addReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddReply().show(getSupportFragmentManager(),"ADD Reply");
            }
        });
    }
    public void onOkPressed(Reply newreply) {
        question.addReply(newreply);
        replyAdapter.notifyDataSetChanged();
    }
}