package com.example.surveygenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (user.id == null){//place holder
            goToCreatUser();
        }
    }
    private void goToCreatUser(){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}