package com.example.surveygenie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

/*Welcome page to create a new user*/
public class CreateUserActivity extends AppCompatActivity {
    SharedPreferences prefs;
    RadioGroup roleGroup;
    private String id;
    private String role;
    private String contact = null;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_create_user);

        /*Generate unique user id*/
        prefs = getSharedPreferences("username",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        id = UUID.randomUUID().toString();
        editor.putString("userID",id);
        editor.commit();
        editor.putBoolean("idCreated",true);
        editor.commit();

        Toast.makeText(CreateUserActivity.this, id, Toast.LENGTH_SHORT).show();

        /*Select user role*/
        roleGroup = (RadioGroup)findViewById(R.id.select_role);
        roleGroup.clearCheck();
        roleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                role = radioButton.getText().toString();
                User user = new User(id,role,contact);
                user.upLoadToDatabase();
                editor.putString("userRole",role);
                editor.commit();
            }
        });

        /*Confirm and go to main activity*/
        final Button confirmRole = findViewById(R.id.confirm_role_button);
        confirmRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateUserActivity.this, role, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
