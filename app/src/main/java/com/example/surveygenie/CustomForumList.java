package com.example.surveygenie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        TextView post = view.findViewById(R.id.post_text);
        post.setText(question.getQuestion());



        /*set up button*/
        return view;
    }
}
