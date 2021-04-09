package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class ViewProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> user;
    TextView userName;
    TextView userRole;
    TextView userContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        //init
        userName = findViewById(R.id.profile_user_id);
        userRole = findViewById(R.id.profile_role);
        userContact = findViewById(R.id.profile_contact);

        //get user
        Intent intent = getIntent();
        String userId = intent.getStringExtra("User");

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                user = document.getData();
                                if (user.get("UserId").equals(userId)) {
                                    userName.setText( (String) user.get("UserId"));
                                    userRole.setText((String)user.get("UserRole"));
                                    userContact.setText((String) user.get("UserContactInfo"));
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        //no back button yet
    }
}