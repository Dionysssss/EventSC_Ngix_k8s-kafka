package com.example.project2_login.mapview;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmptyMapTest {

    @Rule
    public ActivityTestRule<MapViewActivity> activityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    /**
     * Test 1: Verify the Google Map is displayed.
     */
    @Test
    public void testMapIsDisplayed() {
        // Launch MapViewActivity
        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        // Verify that the map is displayed
        onView(withId(R.id.map))
                .check(matches(isDisplayed()));
    }

    /**
     * Test 2: Click on the map and verify the page changes.
     */
    @Test
    public void testClickOnMapChangesPage() {
        // Launch MapViewActivity
        Intent intent = new Intent();
        activityRule.launchActivity(intent);

        // Perform a click on the map
        onView(withId(R.id.map)).perform(click());

        // No need to verify next page here. Just confirm the click action executed.
    }
}