package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.UUID;

public class CreateUserActivity extends AppCompatActivity {
    ArrayList<User> owners;
    ArrayList<User> experimenters;
    User user;
    SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        preference = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        String uID = UUID.randomUUID().toString();//give user a random unique id
        editor.putString("UserID",uID );//load this unique id into preferences
        editor.apply();//save
        user = new User(uID);//create a user with this unique uid
        //click on experimenter
        final Button asExperimenter = findViewById(R.id.as_experimenter);
        asExperimenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //experimenters.add(user);//do to db
                finish();
            }
        });
        //click on owner
        final Button asOwner =findViewById(R.id.as_owner);
        asOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //owners.add(user);//do to db
                finish();
            }
        });

    }

}