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

/*List for replys*/
public class ReplyList extends ArrayAdapter<Reply> {
    private ArrayList replys;
    private Context context;

    /*Filled in replys*/
    public ReplyList(Context context, ArrayList<Reply> replys) {
        super(context,0,replys);
        this.replys = replys;
        this.context = context;
    }

    /*Set view for replys*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.forum_content, parent,false);
        }
        Reply reply = (Reply) replys.get(position);
        TextView replyText = view.findViewById(R.id.post_text);
        replyText.setText(reply.getReply());
        return view;
    }
}
