package com.example.studyapp.activities;

import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.studyapp.R;
import com.example.studyapp.databinding.ActivityTimerBinding;
import com.example.studyapp.taskdb.Task;

import java.util.List;

public class TimerActivity extends AppCompatActivity {

    private ActivityTimerBinding binding;
    private EditText hourText, minuteText, secondsText;
    private String hourString, minutesString, secondsString;
    private Integer hours, minutes, seconds;
    private long startTimeInMillis, timeLeftInMillis, timeElapsedInMillis, endTime;
    private float rHours, savedTime;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private Spinner timerSpinner;
    private boolean timerRunning;
    private String selectedTask;

    private static final String CHANNEL_ID = "timer_channel";
    private NotificationManager notificationManager;

    ViewGroup allViews;

    SharedPreferences pref;
    SharedPreferences.Editor prefEdit;
    SharedPreferences saveTimePrefs;
    SharedPreferences.Editor saveTimePrefsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();

        allViews = (ViewGroup) view;

        create_notification_channel();

        pref = getSharedPreferences(getString(R.string.timer_prefs), Context.MODE_PRIVATE);
        prefEdit = pref.edit();

        saveTimePrefs = getSharedPreferences(getString(R.string.save_time_prefs), Context.MODE_PRIVATE);
        saveTimePrefsEdit = saveTimePrefs.edit();

        //Initialize buttons & edit texts
        startButton = binding.startButton;
        pauseButton = binding.pauseButton;
        resetButton = binding.resetButton;

        hourText = binding.hourText;
        minuteText = binding.minuteText;
        secondsText = binding.secondsText;
        timerSpinner = binding.spinner;


        //Timer button listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerSpinner.getSelectedItem() == null) {
                    Toast.makeText(TimerActivity.this, "Please select a task", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedTask = timerSpinner.getSelectedItem().toString();

                hourString = hourText.getText().toString();
                minutesString = minuteText.getText().toString();
                secondsString = secondsText.getText().toString();

                if(hourString == "" || minutesString == "" || secondsString == "") {
                    Toast.makeText(TimerActivity.this, "Error with time input", Toast.LENGTH_SHORT).show();
                    return;
                }

                hours = Integer.parseInt(hourString);
                minutes = Integer.parseInt(minutesString);
                seconds = Integer.parseInt(secondsString);

                if(hours == 0 && minutes == 0 && seconds == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a value", Toast.LENGTH_LONG).show();
                    return;
                } else if(minutes > 60 || seconds > 60) {
                    Toast.makeText(getApplicationContext(), "Minute or seconds value is too high", Toast.LENGTH_LONG).show();
                    return;
                }

                timeLeftInMillis = calculateStartTime(hours, minutes, seconds);
                startTimeInMillis = timeLeftInMillis;

                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);

                hourText.setEnabled(false);
                minuteText.setEnabled(false);
                secondsText.setEnabled(false);

                startTimer();
            }
        });



        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning) {
                    pauseTimer();

                    pauseButton.setText("Resume");
                } else {
                    startTimer();

                    pauseButton.setText("Pause");
                }

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent mainIntent = getIntent();
        List<Task> spinnerList = (List<Task>) mainIntent.getSerializableExtra("SpinnerList");

        String[] spinnerTitleArray = new String[spinnerList.size()];

        for(int i = 0; i < spinnerList.size(); i++) {
            spinnerTitleArray[i] = spinnerList.get(i).getTitle();
        }



        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTitleArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timerSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveTimePrefsEdit.putLong("millisLeft", timeLeftInMillis);
        saveTimePrefsEdit.putBoolean("timerRunning", timerRunning);
        saveTimePrefsEdit.putLong("endTime", endTime);

        saveTimePrefsEdit.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ObjectAnimator fadeIn;

        for(int i = 0; i < allViews.getChildCount(); i++) {
            allViews.getChildAt(i);
            fadeIn = ObjectAnimator.ofFloat(allViews.getChildAt(i), "alpha", 0f, 1f);
            fadeIn.setDuration(750);
            fadeIn.start();
        }

        timeLeftInMillis = saveTimePrefs.getLong("millisLeft", 0);
        timerRunning = saveTimePrefs.getBoolean("timerRunning", false);

        if(timerRunning) {
            endTime = saveTimePrefs.getLong("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis();

            if(timeLeftInMillis < 0) {
                timeLeftInMillis = 0;
                timerRunning = false;
                updateCountDownText();

                hourText.setText("00");
                minuteText.setText("00");
                secondsText.setText("00");

                startButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);

                hourText.setEnabled(true);
                minuteText.setEnabled(true);
                secondsText.setEnabled(true);

                timerSpinner.setEnabled(true);
            } else {
                startButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.VISIBLE);

                startTimer();
            }
        }


    }

    private void resetTimer() {
        countDownTimer.cancel();
        timerRunning = false;

        timeLeftInMillis = 0;
        hourText.setText("00");
        minuteText.setText("00");
        secondsText.setText("00");

        hourText.setEnabled(true);
        minuteText.setEnabled(true);
        secondsText.setEnabled(true);

        timerSpinner.setEnabled(true);

        startButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);

    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis;

        if(!pref.contains(selectedTask)) {
            prefEdit.putFloat(selectedTask, 0.00f);
            prefEdit.apply();
        }

        timerSpinner.setEnabled(false);

        savedTime = pref.getFloat(selectedTask, 0.00f);
        rHours = 0.00f + savedTime;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                timeElapsedInMillis = startTimeInMillis - timeLeftInMillis;

                rHours += (1.00/60.00)/60.00;

                prefEdit.putFloat(selectedTask, rHours);
                prefEdit.apply();

                updateCountDownText();
            }

            @Override
            public void onFinish() {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                        .setContentTitle("Timer Finished")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        ;

                notificationManager.notify(1, builder.build());

                resetTimer();

            }
        }.start();

        timerRunning = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void updateCountDownText() {
        Integer uHours   = (int) ((timeLeftInMillis / (1000*60*60)) % 24);
        Integer uMinutes = (int) ((timeLeftInMillis / (1000*60)) % 60);
        Integer uSeconds = (int) (timeLeftInMillis / 1000) % 60;

        hourText.setText(String.format("%02d", uHours));
        minuteText.setText(String.format("%02d", uMinutes));
        secondsText.setText(String.format("%02d", uSeconds));
    }

    private long calculateStartTime(int hours, int minutes, int seconds) {
        int hoursInMillis, minsInMillis, secsInMillis, totalMillis;

        hoursInMillis = ((hours * 60) * 60) * 1000;
        minsInMillis = (minutes * 60) * 1000;
        secsInMillis = seconds * 1000;

        totalMillis = hoursInMillis + minsInMillis + secsInMillis;
        return totalMillis;
    }

    private void create_notification_channel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}