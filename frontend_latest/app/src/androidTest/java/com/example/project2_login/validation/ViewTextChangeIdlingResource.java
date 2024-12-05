package com.example.project2_login.validation;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.ViewInteraction;

import static androidx.test.espresso.Espresso.onView;

public class ViewTextChangeIdlingResource implements IdlingResource {

    private final ViewInteraction viewInteraction;
    private final String expectedText;
    private ResourceCallback resourceCallback;

    public ViewTextChangeIdlingResource(ViewInteraction viewInteraction, String expectedText) {
        this.viewInteraction = viewInteraction;
        this.expectedText = expectedText;
    }

    @Override
    public String getName() {
        return this.getClass().getName() + ":" + expectedText;
    }

    @Override
    public boolean isIdleNow() {
        final boolean[] isIdle = {false};
        viewInteraction.check((view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                isIdle[0] = false;
                return;
            }
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                isIdle[0] = textView.getText().toString().equals(expectedText);
            }
        });
        if (isIdle[0] && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return isIdle[0];
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
