package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;



public class UserProfileActivity extends AppCompatActivity{
    private EditText userContact;
    private TextView username;
    SharedPreferences preference;
    Button saving;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username = (TextView)findViewById(R.id.user_id);
        userContact = (EditText)findViewById(R.id.user_contact_information);
        saving = findViewById(R.id.saving);

        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("UserID",null);
        username.setText(userId);
        saving.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String contact = userContact.getText().toString();

            }
        });


    }
}