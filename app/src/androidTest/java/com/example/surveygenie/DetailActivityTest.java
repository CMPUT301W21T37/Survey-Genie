package com.example.surveygenie;

import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class DetailActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule=
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void checkUnsubscribe(){
        MainActivity mainActivity = (MainActivity) solo.getCurrentActivity();
        ListView experimentList = mainActivity.experimentList;
        solo.clickInList(0);
        solo.waitForActivity(".DetailActivity");
        solo.clickOnButton("Unsubscribe");
        solo.goBackToActivity(".MainActivity");
        assertFalse(solo.isTextChecked("testing"));
    }



}
