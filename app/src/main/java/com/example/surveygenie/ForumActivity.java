package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);


        /*initialize question forum */
        forumTitle = (TextView) findViewById(R.id.question_title);
        Intent intent = getIntent();
        String ExperimentDesp = intent.getStringExtra("Description");
        forumTitle.setText(ExperimentDesp);

        forumList = findViewById(R.id.reply_list);
        forumDataList = new ArrayList<>();//load from db related to experiment

        forumAdapter = new CustomForumList(this,forumDataList);
        forumList.setAdapter(forumAdapter);

        /*Show all questions for an experiment*/
        db.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                question = document.getData();
                                if (question.get("Experiment").equals(ExperimentDesp)) {
                                    forumDataList.add(new Question((String) question.get("Question"), (String) question.get("Description")));
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
                                Question question = new Question(edit.getText().toString(),ExperimentDesp);
                                question.uploadtodatabase();
                                forumDataList.add(question);
                                forumAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();

            }
        });

        /*click to forum for reply*/
        forumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ForumActivity.this,ReplyActivity.class);
                // set up question to be passed on
                Question question = forumDataList.get(position);
                intent.putExtra("Question",question.getQuestion());
                startActivity(intent);
            }
        });

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