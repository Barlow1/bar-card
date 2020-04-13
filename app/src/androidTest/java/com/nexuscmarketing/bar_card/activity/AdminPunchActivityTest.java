package com.nexuscmarketing.bar_card.activity;

import android.content.res.Resources;

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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AdminPunchActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule;

    {
        //TODO mock the required attributes to initiate the tests from AdminPunchActivity
        activityRule = new ActivityScenarioRule<>(LoginActivity.class);
    }

    @Test
    public void punchBarCard() {
        onView(ViewMatchers.withId(R.id.login_email))
                .perform(typeText("admin@nexuscmarketing.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password))
                .perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.login_email))
                .check(matches(withText("admin@nexuscmarketing.com")));
        onView(withId(R.id.login_password))
                .check(matches(withText("admin")));
        onView(withId(R.id.login))
                .perform(click());
        onView(withId(R.id.admin_search))
                .perform(click());
        onView(withId(Resources.getSystem().getIdentifier("search_src_text",
                "id", "android")))
                .perform(typeText("test@test.com"), pressImeActionButton());
        onView(withId(R.id.punch_button))
                .perform(click());
        onView(withId(R.id.user_first_name))
                .check(matches(isDisplayed()));
        onView(withId(R.id.user_last_name))
                .check(matches(isDisplayed()));
        onView(withId(R.id.user_email))
                .check(matches(isDisplayed()));
        onView(withId(R.id.user_punches))
                .check(matches(isDisplayed()));
    }
}
