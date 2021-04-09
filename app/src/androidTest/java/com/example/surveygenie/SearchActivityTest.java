package com.example.surveygenie;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;
import static junit.framework.TestCase.assertTrue;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
public class SearchActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule=
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * This test including see each experiment's owner's profile and subscribe experiments
     */
    @Test
    public void checkSearch(){
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.search_click));
        solo.waitForActivity(".SearchActivity");
        solo.clickOnView(solo.getView(R.id.actionSearch));
        solo.enterText(0,"testing");
        solo.clickOnView(solo.getView(R.id.user_profile_search));
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.subscribe_button));
        assertTrue(solo.waitForText("testing"));
    }


    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }

}
