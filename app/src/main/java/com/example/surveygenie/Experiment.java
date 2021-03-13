package com.example.surveygenie;

import java.io.Serializable;

public class Experiment implements Serializable {
    private String region;
    private String trial;
    /*private String type;*/
    private String description;

    Experiment(String region, String trial, String description){
        this.region = region;
        this.trial = trial;
        /*this.type = type;*/
        this.description = description;
    }

    String getRegionName(){ return this.region; }

    String getTrialNumber() { return this.trial; }

    /*String getTypeName() { return this.type; }*/

    String getDescription() { return this.description; }
}
