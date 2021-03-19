package com.example.surveygenie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity {
    ListView replys;
    ArrayAdapter<Reply> replyAdapter;
    ArrayList<Reply> replyDataList;
    TextView replyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Intent intent = getIntent();
        Question question = (Question) intent.getParcelableExtra("Question");
        //use db to retrive question
        //set title
        replyTitle = (TextView) findViewById(R.id.question_title);
        replyTitle.setText(question.getQuestion());
        //set list
        replys = (ListView) findViewById(R.id.reply_list);
        replyDataList = new ArrayList<>();
        replyDataList.addAll(question.getReplys());   //load from db
        replyAdapter = new ReplyList(this,replyDataList);
        replys.setAdapter(replyAdapter);
        //set up add button
        final Button addReplyButton = (Button) findViewById(R.id.add_reply_button);
        addReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ReplyActivity.this);
                builder
                        .setView(view)
                        .setTitle("ADD REPLY")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = view.findViewById(R.id.add_post_editText);
                                Reply reply = new Reply(edit.getText().toString());
                                question.addToReplys(reply);
                                replyDataList.add(reply);
                                replyAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();
            }
        });

        final Button backToQuestion =(Button) findViewById(R.id.back_to_question_button);
        backToQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}