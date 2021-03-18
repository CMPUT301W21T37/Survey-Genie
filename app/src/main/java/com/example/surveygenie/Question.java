package com.example.surveygenie;

import java.util.ArrayList;

public class Question {
    private String question;
    private ArrayList<Reply> replys;


    public Question(String question) {
        this.question = question;
        this.replys = new ArrayList<Reply>();
    }

    public ArrayList<Reply> getReplys() {
        return replys;
    }

    public void addToReplys(Reply reply){
        this.replys.add(reply);
    }

    public String getQuestion() {
        return question;
    }
}
