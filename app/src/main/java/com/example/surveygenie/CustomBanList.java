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
import java.util.List;

public class CustomBanList extends ArrayAdapter<Subscriber> {
    private ArrayList<Subscriber> subscribers;
    private Context context;

    public CustomBanList(Context context, ArrayList<Subscriber> subscribers) {
        super(context,0,subscribers);
        this.context=context;
        this.subscribers=subscribers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.ban_content, parent,false);
        }
        Subscriber subscriber = (Subscriber) subscribers.get(position);
        TextView subscriberName = view.findViewById(R.id.subscriber_name);
        TextView subscriberStatus = view.findViewById(R.id.subscriber_status);

        subscriberName.setText(subscriber.getSubscriber());
        subscriberStatus.setText(subscriber.getStatus());


        return view;
    }
}
