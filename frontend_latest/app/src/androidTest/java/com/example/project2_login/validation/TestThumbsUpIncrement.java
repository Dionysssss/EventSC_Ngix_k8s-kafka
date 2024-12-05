package com.example.project2_login.validation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.project2_login.LoginView;
import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;
import com.google.android.gms.maps.model.Marker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

@RunWith(AndroidJUnit4.class)
public class TestThumbsUpIncrement {

    @Rule
    public ActivityTestRule<LoginView> loginActivityRule =
            new ActivityTestRule<>(LoginView.class);

    @Rule
    public ActivityTestRule<MapViewActivity> mapActivityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    /**
     * Test: Log in, navigate to MapViewActivity, click a marker, and verify thumbs-up increment.
     */
    @Test
    public void testThumbsUpIncrementsCount() throws InterruptedException {
        // Step 1: Perform login
        onView(withId(R.id.email))
                .perform(typeText("chao@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait for navigation to MapViewActivity
        Thread.sleep(3000);

        // Step 2: Get the current activity (should be MapViewActivity)
        Activity currentActivity = getCurrentActivity();
        assertTrue(currentActivity instanceof MapViewActivity);
        MapViewActivity mapActivity = (MapViewActivity) currentActivity;

        // Step 3: Verify the map is displayed
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        // Step 4: Simulate a marker click on the map
        mapActivity.runOnUiThread(() -> {
            if (mapActivity.mMap != null && !mapActivity.markerEventMap.isEmpty()) {
                // Click the first marker
                Marker marker = mapActivity.markerEventMap.keySet().iterator().next();
                if (marker != null) {
                    mapActivity.onMarkerClick(marker); // Simulate marker click
                }
            }
        });

        // Wait for EventDetailsActivity to load
        Thread.sleep(3000);

        // Step 5: Verify thumbs-up functionality
        onView(withId(R.id.thumbs_up_button)).perform(click());

        // Use IdlingResource to wait for the thumbs-up count to update
        ViewInteraction thumbsUpCountInteraction = onView(withId(R.id.thumbs_up_count));
        ViewTextChangeIdlingResource idlingResource = new ViewTextChangeIdlingResource(thumbsUpCountInteraction, "1");

        Thread.sleep(3000);

        // Now perform your check
        onView(withId(R.id.thumbs_up_count)).check(matches(withText("1")));


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