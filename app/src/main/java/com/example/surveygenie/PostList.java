package com.example.surveygenie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostList extends ArrayAdapter<Question> {
    private Context context;
    ArrayList<Question> questions;

    public PostList(  Context context,ArrayList<Question> questions) {
        super(context,0,questions);
        this.questions = questions;
        this.context = context;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.question_layout,parent,false);
        }
        Question question = questions.get(position);
        TextView questionName = view.findViewById(R.id.Question_text);
        questionName.setText(question.getContent());

        Button addReply = view.findViewById(R.id.add_reply_button);
        addReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToReplyActivity(position);
            }
        });
        return view;
    }
    private void goToReplyActivity(int position){
        Intent newintent = new Intent(context,ReplyActivity.class);
        newintent.putExtra("QuestionTitle",questions.get(position).getContent());
        context.startActivity(newintent);
    }
}
