package com.example.studyapp;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.studyapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ActivityTests {

    @Rule
    public ActivityScenarioRule mainActivityRule = new ActivityScenarioRule<>(
            MainActivity.class);

    @Test
    public void createTaskTest() {
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.titleEditText)).perform(
            typeText("Create Test")
        );
        onView(withId(R.id.descriptionEditText)).perform(
                typeText("Create Test description")
        );
        onView(withId(R.id.dialogButton)).perform(click());

        //Can't find a way to automatically check for recyclerview item
    }

    @Test
    public void timerSpinnerTest() {
        onView(withId(R.id.create_button)).perform(click());
        onView(withId(R.id.titleEditText)).perform(
                typeText("Timer Test")
        );
        onView(withId(R.id.descriptionEditText)).perform(
                typeText("Timer Test description")
        );
        onView(withId(R.id.dialogButton)).perform(click());

        onView(withId(R.id.timerButton)).perform(click());
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Timer Test"))).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Timer Test"))));
    }

}
