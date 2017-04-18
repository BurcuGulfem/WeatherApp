package com.archsorceress.weatherapp;

import android.content.Context;
import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.archsorceress.weatherapp.utils.ConnectionHelper;
import com.archsorceress.weatherapp.utils.SharedPreferencesHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    //We need to grant write permission, otherwise the popup won't close and tests will fail
    private void grantWritePermission() {
        // In Android 6.0 will trigger a runtime dialog.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    //We need to clear saved data to test some cases
    private void clearUserSettings(){
        SharedPreferencesHelper helper = new SharedPreferencesHelper( getTargetContext());
        helper.clearPreferences();
    }

    //We can't test the functions if there is no internet connection.
    @Test
    public void checkInternetConnection() throws Exception {
        assertTrue(ConnectionHelper.isConnected(getTargetContext()));
    }

    @Test
    public void useAppContext() throws Exception {
        Context appContext = getTargetContext();
        assertEquals("com.archsorceress.weatherapp", appContext.getPackageName());
    }

    @Test
    public void cityNameEdit_canBeTypedInto() {
        grantWritePermission();
        activityTestRule.launchActivity(null);
        onView(withId(R.id.editText_cityName))
                .perform(typeText("London"), closeSoftKeyboard());
        onView(withId(R.id.editText_cityName)).check(matches(withText("London")));
    }


    @Test
    public void cityNameEdit_resetsAfterAddButtonClick() throws Exception {
        grantWritePermission();
        activityTestRule.launchActivity(null);
        onView(withId(R.id.editText_cityName))
                .perform(typeText("London"), closeSoftKeyboard());
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_cityName)).check(matches(withText("")));
    }

    @Test
    public void recyclerViewCityWeather_addItem() throws Exception {
        grantWritePermission();
        clearUserSettings();
        activityTestRule.launchActivity(null);
        onView(withId(R.id.editText_cityName))
                .perform(typeText("London"), closeSoftKeyboard());
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_cityName))
                .perform(typeText("Amsterdam"), closeSoftKeyboard());
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.recyclerView_cityWeather)).check(new RecyclerViewItemCountAssertion(2));
    }

    @Test
    public void recyclerViewCityWeather_preventAddingDuplicateItem() throws Exception {
        grantWritePermission();
        clearUserSettings();
        activityTestRule.launchActivity(null);
        onView(withId(R.id.editText_cityName))
                .perform(typeText("London"), closeSoftKeyboard());
        onView(withId(R.id.button_add)).perform(click());
        //Let's add the same city again, with a different name. Londra is the Turkish name for London.
        onView(withId(R.id.editText_cityName))
                .perform(typeText("Londra"), closeSoftKeyboard());
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.recyclerView_cityWeather)).check(new RecyclerViewItemCountAssertion(1));
    }

}
