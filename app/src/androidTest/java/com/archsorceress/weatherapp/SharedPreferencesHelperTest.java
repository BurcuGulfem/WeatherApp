package com.archsorceress.weatherapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.archsorceress.weatherapp.utils.SharedPreferencesHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferencesHelperTest {
    @Test
    public void writeAndReadCityIdsTest() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(appContext);

        String s1 = "10,5,21,12";
        sharedPreferencesHelper.writeSavedCityIds(s1);

        String s2= sharedPreferencesHelper.getSavedCityIds();
        assertEquals(s1,s2);
    }
}
