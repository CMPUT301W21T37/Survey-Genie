package com.example.surveygenie;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String role;

    User(String id, String role){
        this.id = id;
        this.role = role;
    }

    String getId(){ return this.id; }

    String getRole() { return this.role; }
}
