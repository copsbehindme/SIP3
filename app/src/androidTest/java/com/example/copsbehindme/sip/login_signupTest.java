package com.example.copsbehindme.sip;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by copsbehindme on 19/05/17.
 */
public class login_signupTest {

    @Rule
    public ActivityTestRule<login_signup> myActivityTestRule = new ActivityTestRule<login_signup>(login_signup.class);

    private login_signup login_signupActivity = null;

    @Before
    public void setUp() throws Exception {
        login_signupActivity = myActivityTestRule.getActivity();

    }
    @Test
    public void testLaunch(){
        View view = login_signupActivity.findViewById(R.id.tryTextView);

        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        login_signup login_signupActivity = null;
    }

}