package com.example.surveygenie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

public class CustomSearchList extends ArrayAdapter<Experiment> {
    private ArrayList<Experiment> experiments;
    private Context context;
    Map<String,Object> subscribers;
    SharedPreferences preference;
    MainActivity activity;
    public CustomSearchList(Context context, ArrayList<Experiment> experiments){
        super(context,0, experiments);
        this.experiments = experiments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.search_content, parent,false);
        }
        Experiment experiment = experiments.get(position);

        TextView name = view.findViewById(R.id.search_name_text);
        TextView description = view.findViewById(R.id.search_description_text);

        name.setText(experiment.getName());
        description.setText(experiment.getDescription());

        /*set up button*/
        FloatingActionButton profile = view.findViewById(R.id.user_profile_search);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewProfileActivity.class);
                // set up question to be passed on
                intent.putExtra("User",experiment.getOwner());
                context.startActivity(intent);
            }
        });

        //subscribe
        preference = context.getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("userID",null);
        Button subButton = view.findViewById(R.id.subscribe_button);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subscriber subscriber = new Subscriber(experiment.getName(),userId,"Permitted");
                subscriber.uploadtodatabase();
                ((Activity)context).finish();
                Intent intent = new Intent((Activity)context,MainActivity.class);
                ((Activity)context).startActivity(intent);
            }
        });
        return view;
    }
}
