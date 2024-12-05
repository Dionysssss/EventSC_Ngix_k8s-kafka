package com.example.project2_login.createEvent;

import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DialogMatcher extends TypeSafeMatcher<Root> {
    @Override
    protected boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        return (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ||
                type == WindowManager.LayoutParams.TYPE_APPLICATION);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is dialog");
    }
}
