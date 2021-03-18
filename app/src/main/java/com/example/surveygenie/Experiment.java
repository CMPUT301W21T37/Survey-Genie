package com.example.surveygenie;

import java.io.Serializable;

public class Experiment implements Serializable {
    private String description;
    private String region;
    private String trial;
    private String type;


    Experiment(String description, String region, String trial, String type){
        this.description = description;
        this.region = region;
        this.trial = trial;
        this.type = type;
    }

    String getDescription() { return this.description; }

    String getRegionName(){ return this.region; }

    String getTrialNumber() { return this.trial; }

    String getTypeName() { return this.type; }
}
