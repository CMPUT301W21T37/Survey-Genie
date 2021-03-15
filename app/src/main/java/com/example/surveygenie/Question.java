package com.example.surveygenie;

import java.util.ArrayList;

public class Question  {
    private User user;
    private String content;
    private ArrayList<Reply> replys;

    public Question(User poster,String description) {
        this.user = poster;
        this.content = description;
    }

    public User getUser() {
        return this.user;
    }

    public String getContent() {
        return content;
    }
    public Question getQuestion(String qTitle) {
        if (this.content == qTitle) {
            return this;
        }else {
            return null;//place holder
        }
    }
    public void addReply(Reply reply){
        this.replys.add(reply);
    }
}
