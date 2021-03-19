package com.example.surveygenie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

public class CreateUserActivity extends AppCompatActivity {
    SharedPreferences prefs;
    RadioGroup roleGroup;
    private String id;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_create_user);

        prefs = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        id = UUID.randomUUID().toString();
        editor.putString("userID",id);
        editor.commit();

        Toast.makeText(CreateUserActivity.this, id, Toast.LENGTH_SHORT).show();

        roleGroup = (RadioGroup)findViewById(R.id.select_role);
        roleGroup.clearCheck();
        roleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                role = radioButton.getText().toString();
            }
        });
        User user = new User(id,role);
        user.upLoadToDatabase();

        final Button confirmRole = findViewById(R.id.confirm_role_button);
        confirmRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("idCreated",true);
                editor.commit();
                finish();
            }
        });

    }
}
