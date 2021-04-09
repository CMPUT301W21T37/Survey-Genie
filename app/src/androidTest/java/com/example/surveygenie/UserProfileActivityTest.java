package com.example.surveygenie;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class UserProfileActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule=
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true);

    @Before
    public void setUp(){
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void checkContact(){
        solo.assertCurrentActivity("Wrong activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.user_profile));
        solo.waitForActivity(".UserProfileActivity");
        solo.enterText((EditText)solo.getView(R.id.user_contact_information),"this is a contact information");
        solo.clickOnButton("Save");
        solo.waitForActivity(".MainActivity");
        solo.clickOnView(solo.getView(R.id.user_profile));
        solo.waitForActivity(".UserProfileActivity");
        assertTrue(solo.waitForText("this is a contact information"));
    }

    @After
    public void tearDown(){
        solo.finishOpenedActivities();
    }
}
