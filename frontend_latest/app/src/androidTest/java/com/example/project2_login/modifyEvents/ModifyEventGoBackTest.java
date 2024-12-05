package com.example.project2_login.modifyEvents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ModifyEventGoBackTest {

    @Rule
    public ActivityTestRule<MapViewActivity> activityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    @Test
    public void testGoBackToEventDetails() throws InterruptedException {
        // Step 1: Launch MapViewActivity and log in
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
        intent.putExtra("userId", 1); // Mock userId
        activityRule.launchActivity(intent);

        // Wait for MapViewActivity to load markers
        Thread.sleep(5000);

        // Step 2: Simulate a marker click to navigate to EventDetailsActivity
        activityRule.getActivity().runOnUiThread(() -> {
            MapViewActivity activity = activityRule.getActivity();
            if (activity.mMap != null && !activity.markerEventMap.isEmpty()) {
                activity.onMarkerClick(activity.markerEventMap.keySet().iterator().next());
            }
        });

        // Wait for navigation to EventDetailsActivity
        Thread.sleep(3000);

        // Step 3: Verify navigation to EventDetailsActivity
        onView(withId(R.id.event_details_layout))
                .check(matches(isDisplayed()));

        // Step 4: Click the "Edit" button to navigate to ModifyEventActivity
        onView(withId(R.id.edit_event_button))
                .perform(click());

        // Wait for navigation to ModifyEventActivity
        Thread.sleep(3000);

        // Step 5: Verify navigation to ModifyEventActivity
        onView(withId(R.id.edit_event_name))
                .check(matches(isDisplayed()));

        // Step 6: Click the "Go Back to Detail" button
        onView(withId(R.id.go_back_button))
                .perform(click());

        // Wait for navigation back to EventDetailsActivity
        Thread.sleep(3000);

        // Step 7: Verify navigation back to EventDetailsActivity
        onView(withId(R.id.event_details_layout))
                .check(matches(isDisplayed()));
    }
}