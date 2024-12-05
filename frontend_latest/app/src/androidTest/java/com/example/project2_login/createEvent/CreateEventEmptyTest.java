package com.example.project2_login.createEvent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.LoginView;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateEventEmptyTest {

    @Rule
    public ActivityTestRule<LoginView> activityRule =
            new ActivityTestRule<>(LoginView.class);

    /**
     * Test: Log in, navigate to MapViewActivity, open CreateEventActivity, and attempt to save an empty event.
     */
    @Test
    public void emptyEventCreation_displaysValidationMessage() throws InterruptedException {
        // Step 1: Log in
        onView(withId(R.id.email))
                .perform(typeText("chao@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait for navigation to MapViewActivity
        Thread.sleep(3000);

        // Step 2: Click a location on the map
        onView(withId(R.id.map)) // Assuming R.id.map is the ID of your map fragment
                .perform(click());

        // Wait for navigation to CreateEventActivity
        Thread.sleep(3000);

        // Step 3: Verify that CreateEventActivity is displayed
        onView(withId(R.id.event_title)).check(matches(isDisplayed()));

        // Step 4: Click the Save Event button without filling any fields
        onView(withId(R.id.save_event_button)).perform(click());

        // Step 5: Verify the Toast message "Please enter the event name and date/time"
        onView(withText("Please enter the event name and date/time"))
                .inRoot(new ToastMatcher()) // Use the ToastMatcher to verify Toast messages
                .check(matches(withText("Please enter the event name and date/time")));
    }
}