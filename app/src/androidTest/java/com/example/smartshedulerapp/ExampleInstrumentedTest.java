package com.example.smartshedulerapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented calendar_background_custom, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under calendar_background_custom.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.smartshedulerapp", appContext.getPackageName());
    }
}
