package com.example.surveygenie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/*List for forums about reply for questions*/
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

        /*set up adapater*/
        TextView post = view.findViewById(R.id.search_name_text);
        post.setText(question.getQuestion());
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set up question to be passed on
                Intent intent = new Intent(context,ReplyActivity.class);
                intent.putExtra("Question",question.getQuestion());
                context.startActivity(intent);
            }
        });
        /*set up button*/
        FloatingActionButton profile = view.findViewById(R.id.user_profile_forum);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewProfileActivity.class);
                // set up question to be passed on
                intent.putExtra("User",question.getUser());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
