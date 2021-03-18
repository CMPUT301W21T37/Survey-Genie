package com.example.surveygenie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    TextView id;
    TextView role;
    EditText contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        id = (TextView)findViewById(R.id.user_id);
        role = (TextView)findViewById(R.id.user_role);
        contact = (EditText)findViewById(R.id.user_contact_inforamtion);

        Intent intent = getIntent();
        String tempId = intent.getStringExtra("Id");
        String tempRole = intent.getStringExtra("Role");
        String tempContact = intent.getStringExtra("Contact");

        id.setText(tempId);
        role.setText(tempRole);
        contact.setText(tempContact);

        final Button saveProfile = findViewById(R.id.save_user_profile);

        /*saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}
