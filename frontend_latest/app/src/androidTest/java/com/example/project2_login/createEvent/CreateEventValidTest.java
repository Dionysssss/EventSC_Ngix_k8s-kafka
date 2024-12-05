package com.example.project2_login.createEvent;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.LoginView;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
public class CreateEventValidTest {

    @Rule
    public ActivityTestRule<LoginView> activityRule =
            new ActivityTestRule<>(LoginView.class);

    /**
     * Test: Navigate from Login to MapView, select a location, and create an event based on that location.
     */
    @Test
    public void testCreateEventFromMap() throws InterruptedException {
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

        // Step 3: Fill in the event details
        onView(withId(R.id.event_title))
                .perform(typeText("USC Meetup"), closeSoftKeyboard());
        onView(withId(R.id.event_description))
                .perform(typeText("This is a meetup event at USC."), closeSoftKeyboard());

        // Step 4: Open the date picker dialog
        onView(withId(R.id.event_date_time)).perform(click());

        // Step 5: Set the date to December 31 of the current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(currentYear, 12, 31));

        // Step 6: Confirm the date selection
        onView(withText("OK"))
                .inRoot(new DialogMatcher()) // Ensure it targets the dialog
                .perform(click());

        // Optional: Wait briefly to ensure the time picker dialog appears
        Thread.sleep(1000);

        // Step 7: Set the time explicitly in the TimePicker
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(22, 0)); // 10:00 PM

        // Step 8: Confirm the time selection
        onView(withText("OK"))
                .inRoot(new DialogMatcher()) // Ensure it targets the dialog
                .perform(click());

        // Pause to allow time confirmation visually
        Thread.sleep(1000);

        // Step 9: Save the event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Step 10: Verify the Toast message "Event created successfully!"
        onView(withText("Event created successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}