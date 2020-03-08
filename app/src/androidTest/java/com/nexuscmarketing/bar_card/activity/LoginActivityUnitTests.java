package com.nexuscmarketing.bar_card.activity;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.nexuscmarketing.bar_card.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginActivityUnitTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule;

    {
        activityRule = new ActivityScenarioRule<>(LoginActivity.class);
    }

    @Test
    public void loginUnsuccessfulWithFakeInformation() {
        onView(ViewMatchers.withId(R.id.login_email))
                .perform(typeText("test_email"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("test_password"), closeSoftKeyboard());
        onView(withId(R.id.login_email))
                .check(matches(withText("test_email")));
        onView(withId(R.id.login_password))
                .check(matches(withText("test_password")));
        onView(withId(R.id.login))
                .perform(click());
        onView(withText(R.string.login_fail)).check(matches(isDisplayed()));
    }

    @Test
    public void loginSuccessful() {
        onView(withId(R.id.login_email))
                .perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("Test"), closeSoftKeyboard());
        onView(withId(R.id.login_email))
                .check(matches(withText("test@test.com")));
        onView(withId(R.id.login_password))
                .check(matches(withText("Test")));
        onView(withId(R.id.login))
                .perform(click());
        onView(withText(R.string.no_cards)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToRegistration() {
        onView(withId(R.id.link_register))
                .perform(click());
        onView(withId(R.id.register))
                .check(matches(isDisplayed()));
        onView(withId(R.id.login))
                .check(doesNotExist());
    }
}
