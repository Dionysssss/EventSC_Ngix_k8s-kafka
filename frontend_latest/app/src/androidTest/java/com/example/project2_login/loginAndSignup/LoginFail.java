package com.example.project2_login.loginAndSignup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2_login.LoginView;
import com.example.project2_login.R;
import com.example.project2_login.ToastMatcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginFail {

    @Rule
    public ActivityScenarioRule<LoginView> activityRule =
            new ActivityScenarioRule<>(LoginView.class);

    @Test
    public void loginFails_displaysFailureToast() {
        // Input non-existent email and password
        onView(withId(R.id.email))
                .perform(typeText("bruce@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());

        // Click the login button
        onView(withId(R.id.login_button)).perform(click());

        // Verify the failure Toast message
        onView(withText("Incorrect email or password."))
                .inRoot(new ToastMatcher())
                .check(matches(withText("Incorrect email or password.")));
    }
}
