package com.nexuscmarketing.bar_card.activity;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.nexuscmarketing.bar_card.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

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
public class RegisterActivityUnitTests {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule;

    {
        activityRule = new ActivityScenarioRule<>(RegisterActivity.class);
    }

    @Test
    public void registrationUnsuccessfulWithMissingInformation() {
        onView(ViewMatchers.withId(R.id.register_email))
                .perform(typeText("test_email"), closeSoftKeyboard());
        onView(withId(R.id.register_password))
                .perform(typeText("test_password"), closeSoftKeyboard());
        onView(withId(R.id.register_email))
                .check(matches(withText("test_email")));
        onView(withId(R.id.register_password))
                .check(matches(withText("test_password")));
        onView(withId(R.id.register))
                .perform(click());
        onView(withText(R.string.login_success)).check(doesNotExist());
    }

    @Test
    public void registrationSuccessful() {
        String randomEmail = UUID.randomUUID().toString();
        onView(ViewMatchers.withId(R.id.first_name_register))
                .perform(typeText("first"), closeSoftKeyboard());
        onView(withId(R.id.last_name_register))
                .perform(typeText("last"), closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.register_email))
                .perform(typeText(randomEmail), closeSoftKeyboard());
        onView(withId(R.id.register_password))
                .perform(typeText("test_password"), closeSoftKeyboard());
        onView(withId(R.id.register_email));
        onView(withId(R.id.register_email))
                .check(matches(withText(randomEmail)));
        onView(withId(R.id.register_password))
                .check(matches(withText("test_password")));
        onView(withId(R.id.first_name_register))
                .check(matches(withText("first")));
        onView(withId(R.id.last_name_register))
                .check(matches(withText("last")));
        onView(withId(R.id.register))
                .perform(click());
        onView(withText(R.string.register_success)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToLogin() {
        onView(withId(R.id.link_login))
                .perform(click());
        onView(withId(R.id.login))
                .check(matches(isDisplayed()));
        onView(withId(R.id.register))
                .check(doesNotExist());
    }
}
