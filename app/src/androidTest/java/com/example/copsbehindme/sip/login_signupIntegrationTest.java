package com.example.copsbehindme.sip;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by copsbehindme on 20/05/17.
 */
public class login_signupIntegrationTest {
    @Rule
    public ActivityTestRule<login_signup> login_signupActivity = new ActivityTestRule<login_signup>(login_signup.class);

    private login_signup lsActivity = null;
    Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(login_signup.class.getName(),null,false);
    @Before
    public void setUp() throws Exception {
        lsActivity = login_signupActivity.getActivity();
    }
    @Test
    public void launchIntegrationTest(){
        assertNotNull(lsActivity.findViewById(R.id.tryButton));
        onView(withId(R.id.tryButton)).perform(click());
        Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 3000);
        assertNotNull(nextActivity);
    }

    @After
    public void tearDown() throws Exception {
        login_signup lsActivity = null;
    }

}