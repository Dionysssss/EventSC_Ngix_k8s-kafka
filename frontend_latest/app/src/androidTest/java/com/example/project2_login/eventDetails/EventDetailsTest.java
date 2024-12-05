package com.example.project2_login.eventDetails;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.EventDetailsActivity;
import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;
import com.google.android.gms.maps.model.Marker;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDetailsTest {

    @Rule
    public ActivityTestRule<MapViewActivity> mapActivityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    @Rule
    public ActivityTestRule<EventDetailsActivity> eventDetailsActivityRule =
            new ActivityTestRule<>(EventDetailsActivity.class, true, false);

    @Test
    public void testClickMarkerNavigatesToEventDetails() throws InterruptedException {
        // Step 1: Launch MapViewActivity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
        intent.putExtra("userId", 1); // Pass a mock userId
        mapActivityRule.launchActivity(intent);

        // Wait for MapViewActivity to initialize and load markers
        Thread.sleep(5000); // Adjust the sleep time as needed for your environment

        // Step 2: Simulate a marker click
        mapActivityRule.getActivity().runOnUiThread(() -> {
            MapViewActivity activity = mapActivityRule.getActivity();
            if (activity.mMap != null && !activity.markerEventMap.isEmpty()) {
                // Click the first marker in the markerEventMap
                Marker marker = activity.markerEventMap.keySet().iterator().next();
                if (marker != null) {
                    // Simulate marker click
                    activity.onMarkerClick(marker);
                }
            }
        });

        // Wait for navigation to EventDetailsActivity
        Thread.sleep(3000); // Adjust the sleep time as needed

        // Step 3: Verify navigation to EventDetailsActivity
        onView(withId(R.id.event_details_layout)) // Replace with the actual ID in EventDetailsActivity
                .check(matches(isDisplayed()));
    }

    @Test
    public void testPostOneComment() throws InterruptedException {
        // Step 1: Launch EventDetailsActivity with required intent extras
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventDetailsActivity.class);
        intent.putExtra("event_id", 1); // Pass a mock event_id
        intent.putExtra("user_id", 1);  // Pass a mock user_id
        eventDetailsActivityRule.launchActivity(intent);

        // Wait for the activity to load event details
        Thread.sleep(3000);

        // Step 2: Type a comment
        String testComment = "This is a test comment";

        onView(withId(R.id.new_comment_edit_text))
                .perform(typeText(testComment), closeSoftKeyboard());

        // Step 3: Click the Post Comment button
        onView(withId(R.id.post_comment_button))
                .perform(click());

        // Step 4: Verify the "Comment Posted" Toast message
        onView(withText("Comment posted"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        // Wait briefly to allow the comment to be added to the list
        Thread.sleep(2000);

    }

    @Test
    public void testPostMultipleComments() throws InterruptedException {
        // Step 1: Launch EventDetailsActivity with required intent extras
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventDetailsActivity.class);
        intent.putExtra("event_id", 1); // Pass a mock event_id
        intent.putExtra("user_id", 1);  // Pass a mock user_id
        eventDetailsActivityRule.launchActivity(intent);

        // Wait for the activity to load event details
        Thread.sleep(3000);

        // Define the comments to post
        String[] testComments = {
                "First test comment",
                "Second test comment",
                "Third test comment"
        };

        for (String testComment : testComments) {
            // Step 2: Type a comment
            onView(withId(R.id.new_comment_edit_text))
                    .perform(typeText(testComment), closeSoftKeyboard());

            // Step 3: Click the Post Comment button
            onView(withId(R.id.post_comment_button))
                    .perform(click());

            // Step 4: Verify the "Comment posted" Toast message
            onView(withText("Comment posted"))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));

            // Wait briefly to allow the comment to be added to the list
            Thread.sleep(2000);
        }
    }
}