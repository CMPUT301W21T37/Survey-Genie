package com.example.surveygenie;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Page for forums about questions of an experiment,can try to reply to it*/
public class ForumActivity extends AppCompatActivity {

    TextView forumTitle;
    ListView forumList;
    ArrayAdapter<Question> forumAdapter;
    ArrayList<Question> forumDataList;
    Map<String,Object> question;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);


        /*initialize question forum */
        forumTitle = (TextView) findViewById(R.id.question_title);
        Intent intent = getIntent();
        String ExperimentName = intent.getStringExtra("Experiment Name");
        forumTitle.setText(ExperimentName);

        forumList = findViewById(R.id.reply_list);
        forumDataList = new ArrayList<>();//load from db related to experiment

        forumAdapter = new CustomForumList(this,forumDataList);
        forumList.setAdapter(forumAdapter);

        //get current username
        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("userID",null);
        /*Show all questions for an experiment*/
        db.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                question = document.getData();
                                if (question.get("Experiment").equals(ExperimentName)) {
                                    forumDataList.add(new Question((String) question.get("Question"), (String) question.get("Experiment"),(String)question.get("User")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                            forumAdapter = new CustomForumList(ForumActivity.this, forumDataList);
                            forumList.setAdapter(forumAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        /*Add reply for question*/
        final Button addpostButton = (Button) findViewById(R.id.add_question_button);

        String role = preference.getString("userRole",null);
        if (role.equals("Owner")){
            addpostButton.setVisibility(View.GONE);
        }

        addpostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ForumActivity.this);
                builder
                        .setView(view)
                        .setTitle("ADD QUESTION")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = view.findViewById(R.id.add_post_editText);
                                Question question = new Question(edit.getText().toString(),ExperimentName,userId);
                                question.uploadtodatabase();
                                forumDataList.add(question);
                                forumAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();

            }
        });

        /*click to forum for reply*/


        /*Return to the previous page*/
        final Button backToOptionButton =(Button) findViewById(R.id.back_to_option_button);
        backToOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}