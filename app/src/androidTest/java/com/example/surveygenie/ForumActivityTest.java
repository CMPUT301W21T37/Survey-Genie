package com.example.surveygenie;

import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;

public class ForumActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule=
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * This test is for experimenters only
     */
    @Test
    public void checkAddQuestion(){
        MainActivity mainActivity = (MainActivity) solo.getCurrentActivity();
        ListView experimentList = mainActivity.experimentList;
        solo.clickInList(0);
        solo.waitForActivity(".DetailActivity");
        solo.clickOnButton("Forum");
        solo.waitForActivity(".ForumActivity");
        solo.clickOnButton("Add Question");
        solo.clickOnView(solo.getView(android.R.id.button2));
    }
    @Test
    public void checkAddReply(){
        MainActivity mainActivity = (MainActivity) solo.getCurrentActivity();
        ListView experimentList = mainActivity.experimentList;
        solo.clickInList(0);
        solo.waitForActivity(".DetailActivity");
        solo.clickOnButton("Forum");
        solo.waitForActivity(".ForumActivity");
        solo.clickInList(0);
        solo.clickOnButton("Add Reply");
        solo.clickOnView(solo.getView(android.R.id.button2));
    }
}

