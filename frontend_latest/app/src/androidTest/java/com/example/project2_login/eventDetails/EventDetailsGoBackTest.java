package com.example.project2_login.eventDetails;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.project2_login.LoginView;
import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EventDetailsGoBackTest {

    @Rule
    public ActivityTestRule<LoginView> loginActivityRule =
            new ActivityTestRule<>(LoginView.class);

    /**
     * Test: Log in, navigate to MapView, open EventDetailsActivity, and go back to MapViewActivity.
     */
    @Test
    public void testGoBackToMap() throws InterruptedException {
        // Step 1: Perform login
        onView(withId(R.id.email))
                .perform(ViewActions.typeText("chao@usc.edu"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait for navigation to MapViewActivity
        Thread.sleep(3000);

        // Step 2: Get the current activity (should be MapViewActivity)
        Activity currentActivity = getCurrentActivity();
        assertTrue(currentActivity instanceof MapViewActivity);
        MapViewActivity mapActivity = (MapViewActivity) currentActivity;

        // Step 3: Click on a marker to open EventDetailsActivity
        mapActivity.runOnUiThread(() -> {
            if (mapActivity.mMap != null && !mapActivity.markerEventMap.isEmpty()) {
                // Click the first marker
                com.google.android.gms.maps.model.Marker marker = mapActivity.markerEventMap.keySet().iterator().next();
                if (marker != null) {
                    mapActivity.onMarkerClick(marker); // Simulate marker click
                }
            }
        });

        // Wait for EventDetailsActivity to load
        Thread.sleep(3000);

        // Verify the EventDetailsActivity is displayed
        onView(withId(R.id.event_details_layout)).check(matches(isDisplayed()));

        // Step 4: Click "Back to Map" button
        onView(withId(R.id.back_to_map_button)).perform(click());

        // Verify that we are back on MapViewActivity
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    // Helper method to get the current activity
    private Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
            if (!activities.isEmpty()) {
                currentActivity[0] = activities.iterator().next();
            }
        });
        return currentActivity[0];
    }
}