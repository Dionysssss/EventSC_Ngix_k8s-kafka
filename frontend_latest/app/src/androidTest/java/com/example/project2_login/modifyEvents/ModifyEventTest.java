//package com.example.project2_login.modifyEvents;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.clearText;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.typeText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.RootMatchers.isDialog;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.not;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import androidx.test.core.app.ApplicationProvider;
//import androidx.test.espresso.ViewAction;
//import androidx.test.espresso.UiController;
//import androidx.test.espresso.contrib.PickerActions;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.rule.ActivityTestRule;
//
//import com.example.project2_login.MapViewActivity;
//import com.example.project2_login.R;
//import com.example.project2_login.ToastMatcher;
//import com.google.android.gms.maps.model.Marker;
//
//import org.hamcrest.Matcher;
//import org.hamcrest.Matchers;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.Calendar;
//
//@RunWith(AndroidJUnit4.class)
//public class ModifyEventTest {
//
//    @Rule
//    public ActivityTestRule<MapViewActivity> activityRule =
//            new ActivityTestRule<>(MapViewActivity.class, true, false);
//
//    @Test
//    public void testModifyEvent() throws InterruptedException {
//        // Step 1: Launch MapViewActivity
//        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
//        intent.putExtra("userId", 1); // Pass a mock userId
//        activityRule.launchActivity(intent);
//
//        // Wait for MapViewActivity to initialize and load markers
//        Thread.sleep(5000); // Adjust the sleep time as needed for your environment
//
//        // Step 2: Simulate a marker click
//        activityRule.getActivity().runOnUiThread(() -> {
//            MapViewActivity activity = activityRule.getActivity();
//            if (activity.mMap != null && !activity.markerEventMap.isEmpty()) {
//                // Click the first marker in the markerEventMap
//                Marker marker = activity.markerEventMap.keySet().iterator().next();
//                if (marker != null) {
//                    // Simulate marker click
//                    activity.onMarkerClick(marker);
//                }
//            }
//        });
//
//        // Wait for navigation to EventDetailsActivity
//        Thread.sleep(3000); // Adjust the sleep time as needed
//
//        // Step 3: Verify navigation to EventDetailsActivity
//        onView(withId(R.id.event_details_layout)) // Replace with the actual ID in EventDetailsActivity
//                .check(matches(isDisplayed()));
//
//        // Step 4: Click the "Edit" button in EventDetailsActivity
//        onView(withId(R.id.edit_event_button))
//                .perform(click());
//
//        // Wait for navigation to ModifyEventActivity
//        Thread.sleep(3000); // Adjust the sleep time as needed
//
//        // Step 5: Modify the event details
//        onView(withId(R.id.edit_event_name))
//                .perform(clearText(), typeText("Updated Event Name"), closeSoftKeyboard());
//        onView(withId(R.id.edit_event_description))
//                .perform(clearText(), typeText("Updated Description"), closeSoftKeyboard());
//
//        // Modify the date
//        onView(withId(R.id.edit_event_date)).perform(click());
//
//        // Set a new date in the date picker
//        Calendar calendar = Calendar.getInstance();
//        int currentYear = calendar.get(Calendar.YEAR);
//
//        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
//                .perform(PickerActions.setDate(currentYear + 1, 1, 15)); // January 15 of next year
//
//        // Confirm the date selection
//        onView(withText("OK"))
//                .inRoot(isDialog())
//                .perform(click());
//
//        // Modify the time
//        onView(withId(R.id.edit_event_time)).perform(click());
//
//        // Set a new time in the time picker
//        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
//                .perform(PickerActions.setTime(21, 30)); // 9:30 PM
//
//        // Confirm the time selection
//        onView(withText("OK"))
//                .inRoot(isDialog())
//                .perform(click());
//
//        // Pause briefly
//        Thread.sleep(1000);
//
//        // Step 6: Click on the map to change the location
//        // Get the original location text
//        String[] originalLocationText = new String[1];
//        onView(withId(R.id.edit_event_location))
//                .perform(new GetTextAction(originalLocationText));
//
//        // Attempt to click on the map view
//        onView(withId(R.id.edit_map))
//                .perform(click());
//
//        // Wait for the location to update
//        Thread.sleep(2000);
//
//        // Get the new location text
//        String[] newLocationText = new String[1];
//        onView(withId(R.id.edit_event_location))
//                .perform(new GetTextAction(newLocationText));
//
//        // Step 7: Verify that the location has changed
//        if (originalLocationText[0].equals(newLocationText[0])) {
//            throw new AssertionError("Location did not change after clicking on the map.");
//        }
//
//        // Step 8: Save the updated event
//        onView(withId(R.id.save_event_button))
//                .perform(click());
//
//        // Step 9: Verify the Toast message "Event updated successfully!"
//        onView(withText("Event updated successfully!"))
//                .inRoot(new ToastMatcher())
//                .check(matches(isDisplayed()));
//    }
//
//    /**
//     * Custom ViewAction to get the text from a TextView.
//     */
//    public static class GetTextAction implements ViewAction {
//        private String[] textHolder;
//
//        public GetTextAction(String[] textHolder) {
//            this.textHolder = textHolder;
//        }
//
//        @Override
//        public Matcher<View> getConstraints() {
//            return isDisplayed(); // Ensure the view is displayed
//        }
//
//        @Override
//        public String getDescription() {
//            return "Get text from TextView";
//        }
//
//        @Override
//        public void perform(UiController uiController, View view) {
//            if (view instanceof TextView || view instanceof EditText) {
//                textHolder[0] = ((TextView) view).getText().toString();
//            } else {
//                throw new RuntimeException("The view is not a TextView or EditText");
//            }
//        }
//    }
//}

package com.example.project2_login.modifyEvents;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.UiController;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.project2_login.MapViewActivity;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;
import com.google.android.gms.maps.model.Marker;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
public class ModifyEventTest {

    @Rule
    public ActivityTestRule<MapViewActivity> activityRule =
            new ActivityTestRule<>(MapViewActivity.class, true, false);

    private void navigateToModifyEventActivity() throws InterruptedException {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MapViewActivity.class);
        intent.putExtra("userId", 1); // Pass a mock userId
        activityRule.launchActivity(intent);

        // Wait for MapViewActivity to initialize and load markers
        Thread.sleep(5000);

        // Simulate a marker click
        activityRule.getActivity().runOnUiThread(() -> {
            MapViewActivity activity = activityRule.getActivity();
            if (activity.mMap != null && !activity.markerEventMap.isEmpty()) {
                Marker marker = activity.markerEventMap.keySet().iterator().next();
                if (marker != null) {
                    activity.onMarkerClick(marker);
                }
            }
        });

        // Wait for navigation to EventDetailsActivity
        Thread.sleep(3000);

        // Click the "Edit" button in EventDetailsActivity
        onView(withId(R.id.edit_event_button))
                .perform(click());

        // Wait for navigation to ModifyEventActivity
        Thread.sleep(3000);
    }

    @Test
    public void testModifyEventName() throws InterruptedException {
        navigateToModifyEventActivity();

        // Modify the event name
        onView(withId(R.id.edit_event_name))
                .perform(clearText(), typeText("Updated Event Name"), closeSoftKeyboard());

        // Save the updated event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Verify the Toast message
        onView(withText("Event updated successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testModifyEventDescription() throws InterruptedException {
        navigateToModifyEventActivity();

        // Modify the event description
        onView(withId(R.id.edit_event_description))
                .perform(clearText(), typeText("Updated Description"), closeSoftKeyboard());

        // Save the updated event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Verify the Toast message
        onView(withText("Event updated successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testModifyEventDate() throws InterruptedException {
        navigateToModifyEventActivity();

        // Modify the date
        onView(withId(R.id.edit_event_date)).perform(click());

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(currentYear + 1, 1, 15));

        onView(withText("OK"))
                .inRoot(isDialog())
                .perform(click());

        // Save the updated event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Verify the Toast message
        onView(withText("Event updated successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testModifyEventTime() throws InterruptedException {
        navigateToModifyEventActivity();

        // Modify the time
        onView(withId(R.id.edit_event_time)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(21, 30));

        onView(withText("OK"))
                .inRoot(isDialog())
                .perform(click());

        // Save the updated event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Verify the Toast message
        onView(withText("Event updated successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testModifyEventLocation() throws InterruptedException {
        navigateToModifyEventActivity();

        // Get the original location text
        String[] originalLocationText = new String[1];
        onView(withId(R.id.edit_event_location))
                .perform(new GetTextAction(originalLocationText));

        // Click on the map to change the location
        onView(withId(R.id.edit_map))
                .perform(click());

        // Wait for the location to update
        Thread.sleep(2000);

        // Get the new location text
        String[] newLocationText = new String[1];
        onView(withId(R.id.edit_event_location))
                .perform(new GetTextAction(newLocationText));

        // Verify the location has changed
        if (originalLocationText[0].equals(newLocationText[0])) {
            throw new AssertionError("Location did not change after clicking on the map.");
        }

        // Save the updated event
        onView(withId(R.id.save_event_button))
                .perform(click());

        // Verify the Toast message
        onView(withText("Event updated successfully!"))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    /**
     * Custom ViewAction to get the text from a TextView.
     */
    public static class GetTextAction implements ViewAction {
        private String[] textHolder;

        public GetTextAction(String[] textHolder) {
            this.textHolder = textHolder;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isDisplayed();
        }

        @Override
        public String getDescription() {
            return "Get text from TextView";
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (view instanceof TextView || view instanceof EditText) {
                textHolder[0] = ((TextView) view).getText().toString();
            } else {
                throw new RuntimeException("The view is not a TextView or EditText");
            }
        }
    }
}