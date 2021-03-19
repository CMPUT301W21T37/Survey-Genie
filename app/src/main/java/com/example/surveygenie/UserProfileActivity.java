package com.example.surveygenie;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

/*Page for user profile*/
public class UserProfileActivity extends AppCompatActivity{
    private TextView username;
    private TextView userRole;
    private EditText userContact;
    SharedPreferences preference;
    Button saving;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username = (TextView)findViewById(R.id.user_id);
        userRole = (TextView)findViewById(R.id.user_role);
        userContact = (EditText)findViewById(R.id.user_contact_information);
        saving = findViewById(R.id.saving);

        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("userID",null);
        String role = preference.getString("userRole",null);
        String contact = preference.getString("contact",null);

        username.setText(userId);
        userRole.setText(role);
        userContact.setText(contact);

        /*Updating editted contact information to the firestore*/
        db.collection("users").document(userId)
                .update("UserContactInfo", contact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        /*Saving editted contact information*/
        saving.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preference.edit();
                String contact = userContact.getText().toString();
                editor.putString("contact",contact);
                editor.commit();
            }
        });


    }
}