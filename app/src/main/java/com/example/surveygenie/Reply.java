package com.example.surveygenie;

public class Reply  {
    private User user;
    private String content;
    public Reply(User poster,String description){
        this.user = poster;
        this.content = description;
    }
    public User getUser() {
        return this.user;
    }
    public String getContent() {
        return this.content;
    }
}
