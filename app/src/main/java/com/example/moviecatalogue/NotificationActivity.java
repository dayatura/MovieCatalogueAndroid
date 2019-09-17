package com.example.moviecatalogue;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.example.moviecatalogue.notifications.AlarmReceiver;

public class NotificationActivity extends AppCompatActivity {

    public static String DAILY_PREF_KEY = "DAILY_PREF_KEY";
    public static String RELEASE_PREF_KEY = "RELEASE_PREF_KEY";
    SharedPreferences sharedPref;
    private Switch swDaily;
    private Switch swRelease;
    private Switch.OnClickListener mSwitchCliked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPref.edit();
            final AlarmReceiver alarmReceiver = new AlarmReceiver();

            switch (view.getId()) {
                case R.id.switch_repeating_notifications:
                    editor.putBoolean(DAILY_PREF_KEY, swDaily.isChecked());
                    if (swDaily.isChecked()) {
                        alarmReceiver.setRepeatingAlarm(view.getContext(), AlarmReceiver.TYPE_REPEATING, "07:00", "Daily Update");
                    } else {
                        alarmReceiver.cancelAlarm(view.getContext(), AlarmReceiver.TYPE_REPEATING);
                    }
                    break;

                case R.id.switch_release_notifications:
                    editor.putBoolean(RELEASE_PREF_KEY, swRelease.isChecked());
                    if (swRelease.isChecked()) {
                        alarmReceiver.setReleaseAlarm(view.getContext(), AlarmReceiver.TYPE_RELEASE, "08:00");
                    } else {
                        alarmReceiver.cancelAlarm(view.getContext(), AlarmReceiver.TYPE_RELEASE);
                    }
                    break;

            }

            editor.apply();
            return;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        boolean notificationDefault = getResources().getBoolean(R.bool.notification_default);
        boolean daily_notif = sharedPref.getBoolean(DAILY_PREF_KEY, notificationDefault);
        boolean release_notif = sharedPref.getBoolean(RELEASE_PREF_KEY, notificationDefault);

        swDaily = findViewById(R.id.switch_repeating_notifications);
        swDaily.setChecked(daily_notif);
        swDaily.setOnClickListener(mSwitchCliked);

        swRelease = findViewById(R.id.switch_release_notifications);
        swRelease.setChecked(release_notif);
        swRelease.setOnClickListener(mSwitchCliked);
    }


}
