package com.example.surveygenie;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class CreateUserActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<CreateUserActivity> rule =
            new ActivityTestRule<CreateUserActivity>(CreateUserActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    /**
     * This is for owners only
     */
    public void checkAddExperiment() {
        solo.clickOnRadioButton(0);
        solo.clickOnButton("Confirm");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.user_profile));
        solo.waitForActivity(".UserProfileActivity");
        assertTrue(solo.waitForText("Owner"));
    }
    @Test
    /**
     * This is for experimenter only
     */
    public void checkAddExperiment2() {
        solo.clickOnRadioButton(1);
        solo.clickOnButton("Confirm");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.user_profile));
        solo.waitForActivity(".UserProfileActivity");
        assertTrue(solo.waitForText("Experimenter"));
    }
    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
