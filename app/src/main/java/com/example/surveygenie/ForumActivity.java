package com.example.surveygenie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    TextView forumTitle;
    ListView forumList;
    ArrayAdapter<Question> forumAdapter;
    ArrayList<Question> forumDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);


        //initialize forum
        //title
        forumTitle = (TextView) findViewById(R.id.forum_title);
        Intent intent = getIntent();
        String tempDesp = intent.getStringExtra("Description");
        forumTitle.setText(tempDesp);
        //listview
        forumList = findViewById(R.id.post_list);
        forumDataList = new ArrayList<>();
        Question q = new Question("Why");
        forumDataList.add(q);
        forumAdapter = new CustomForumList(this,forumDataList);
        forumList.setAdapter(forumAdapter);
        //button
        final Button addpostButton = (Button) findViewById(R.id.add_question_button);
        addpostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumActivity.this);
                builder
                        .setView(view)
                        .setTitle("ADD QUESTION")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = view.findViewById(R.id.add_post_editText);
                                Question question = new Question(edit.getText().toString());
                                forumDataList.add(question);
                                forumAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();

            }
        });

    }
}