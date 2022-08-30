package com.example.studyapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.studyapp.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroSlider extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome", "A productivity app",
                R.drawable.logo_final, ContextCompat.getColor(getApplicationContext(), R.color.purple_500)));

        addSlide(AppIntroFragment.newInstance("To Do List", "Keep track of your tasks using the to do list", R.drawable.to_do_list,
                ContextCompat.getColor(getApplicationContext(), R.color.purple_500)));

        addSlide(AppIntroFragment.newInstance("Timer", "Keep track of the time spent on each activity", R.drawable.timer_slide,
                ContextCompat.getColor(getApplicationContext(), R.color.purple_500)));
        setScrollDurationFactor(2);


    }


    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    public static final String COMPLETED_ONBOARDING_PREF_NAME = "Onboarding Completed";

    public void finishTutorial(){
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        sharedPreferencesEditor.putBoolean(
                COMPLETED_ONBOARDING_PREF_NAME, true);
        sharedPreferencesEditor.apply();

        finish();
    }
}
