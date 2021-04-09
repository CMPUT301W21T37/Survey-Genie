package com.example.surveygenie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/*Page for reply of question*/
public class ReplyActivity extends AppCompatActivity {
    ListView replys;
    ArrayAdapter<Reply> replyAdapter;
    ArrayList<Reply> replyDataList;
    TextView replyTitle;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Object> reply;
    SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);



        /*get Title from last acitivity*/
        Intent intent = getIntent();
        String questionTitle = intent.getStringExtra("Question");
        /*set title*/
        replyTitle = (TextView) findViewById(R.id.question_title);
        replyTitle.setText(questionTitle);
        /*set list for replys*/
        replys = (ListView) findViewById(R.id.reply_list);
        replyDataList = new ArrayList<>();

        replyAdapter = new ReplyList(this,replyDataList);
        replys.setAdapter(replyAdapter);

        //get current username
        preference = getSharedPreferences("username",MODE_PRIVATE);
        String userId= preference.getString("userID",null);

        /*Get and display replys of a question*/
        db.collection("replys")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                reply = document.getData();
                                if (reply.get("Question").equals(questionTitle)) {
                                    replyDataList.add(new Reply((String) reply.get("Reply"), (String) reply.get("Question"),(String) reply.get("User")));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                            replyAdapter = new ReplyList(ReplyActivity.this, replyDataList);
                            replys.setAdapter(replyAdapter);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        /*Add reply for a question*/
        final Button addReplyButton = (Button) findViewById(R.id.add_reply_button);
        addReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.add_post_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ReplyActivity.this);
                builder
                        .setView(view)
                        .setTitle("ADD REPLY")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText edit = view.findViewById(R.id.add_post_editText);
                                Reply reply = new Reply(edit.getText().toString(),questionTitle,userId);
                                reply.uploadtodatabase();
                                replyDataList.add(reply);
                                replyAdapter.notifyDataSetChanged();
                            }
                        }).create();
                builder.show();
            }
        });

        /*Go back to the previous page*/
        final Button backToQuestion =(Button) findViewById(R.id.back_to_question_button);
        backToQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}