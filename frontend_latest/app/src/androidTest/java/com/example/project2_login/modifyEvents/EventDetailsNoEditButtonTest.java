package com.example.project2_login.modifyEvents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDetailsNoEditButtonTest {

    @Rule
    public ActivityTestRule<MapViewActivity> activityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    @Test
    public void testNoEditButtonForNonCreator() throws InterruptedException {
        // Step 1: Launch MapViewActivity and log in as a non-creator user
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
        intent.putExtra("userId", 2); // Non-creator user ID
        activityRule.launchActivity(intent);

        // Wait for MapViewActivity to initialize
        Thread.sleep(3000);

        // Step 2: Verify the map is displayed
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        // Step 3: Simulate a marker click
        activityRule.getActivity().runOnUiThread(() -> {
            MapViewActivity mapActivity = activityRule.getActivity();
            if (mapActivity.mMap != null && !mapActivity.markerEventMap.isEmpty()) {
                mapActivity.onMarkerClick(mapActivity.markerEventMap.keySet().iterator().next());
            }
        });

        // Wait for navigation to EventDetailsActivity
        Thread.sleep(3000);

        // Step 4: Verify navigation to EventDetailsActivity
        onView(withId(R.id.event_details_layout))
                .check(matches(isDisplayed()));

        // Step 5: Verify the "Edit Event" button is not visible
        onView(withId(R.id.edit_event_button))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}