package com.beacon.afterui.test;

import com.beacon.afterui.views.LandingActivity;

import android.test.ActivityInstrumentationTestCase2;

public class AfterUITest extends ActivityInstrumentationTestCase2<LandingActivity> {

    public AfterUITest() {
        super("com.beacon.afterui", LandingActivity.class);
    }
 
    public void testSanity() {
        assertEquals(2, 1 + 1);
    }

}
