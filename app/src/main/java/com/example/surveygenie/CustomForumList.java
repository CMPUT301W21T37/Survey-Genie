package com.example.surveygenie;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class CustomForumList extends ArrayAdapter<Question> {

    private ArrayList<Question> posts;
    private Context context;

    public CustomForumList( Context context,ArrayList<Question> posts) {
        super(context, 0, posts);
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.forum_content, parent,false);
        }

        Question question = posts.get(position);

        //set up titlle
        TextView post = view.findViewById(R.id.question_text);
        post.setText(question.getQuestion());
/*
        //set up reply list


        //set up button
        final Button addReplyButton = view.findViewById(R.id.add_reply_button);
        addReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(context).inflate(R.layout.add_post_layout, parent,false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                replyAdapter.notifyDataSetChanged();
                            }
                        }).create();

            }
        });*/
        return view;
    }
}
