package com.example.surveygenie;

import android.widget.EditText;
import android.widget.ListView;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;



import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule=
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    /**
     * This is for owners only
     */
    public void checkAddExperiment(){
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);
        solo.clickOnButton("ADD EXPERIMENT");
        solo.waitForDialogToOpen();
        solo.enterText((EditText)solo.getView(R.id.add_experiment_name),"testing5");
        solo.enterText((EditText)solo.getView(R.id.add_experiment_description),"00000");
        solo.enterText((EditText)solo.getView(R.id.add_experiment_region),"1");
        solo.enterText((EditText)solo.getView(R.id.add_experiment_trial),"1");
        solo.clickOnRadioButton(1);
        solo.clickOnView(solo.getView(android.R.id.button1));
        solo.waitForDialogToClose();
    }

    @Test
    /**
     * This is for owners only
     */
    public void checkUnpublished(){
        MainActivity mainActivity = (MainActivity) solo.getCurrentActivity();
        ListView experimentList = mainActivity.experimentList;
        solo.clickLongInList(0);
        solo.waitForDialogToOpen();
        solo.clickOnView(solo.getView(android.R.id.button1));

    }


    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }

}

