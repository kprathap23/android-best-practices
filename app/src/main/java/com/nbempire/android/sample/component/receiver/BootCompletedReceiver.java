package com.nbempire.android.sample.component.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.nbempire.android.sample.Application;
import com.nbempire.android.sample.component.service.ItemTrackerService;

public class BootCompletedReceiver extends BroadcastReceiver {

    /**
     * Used for log messages.
     */
    private static final String TAG = "BootCompletedReceiver";

    private static final String FIRST_LOAD = "firstLoad";

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive...");
        setupAlarms(context);
    }

    public static void setupApplicationIfNeeded(Context context) {
        Log.v(TAG, "setupApplicationIfNeeded...");

        SharedPreferences sharedPreferences = context.getSharedPreferences(Application.Keys.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(FIRST_LOAD, true)) {
            Log.d(TAG, "Setting up application...");

            setupAlarms(context);

            sharedPreferences.edit().putBoolean(FIRST_LOAD, false).apply();
        }
    }

    private static void setupAlarms(Context context) {
        Log.i(TAG, "Setting up service: " + ItemTrackerService.class.getName() + " to run between " + Application.Keys.CHECK_TRACKED_ITEMS_INTERVAL_SECONDS + "seconds");

        Intent serviceIntent = new Intent(context, ItemTrackerService.class);
        serviceIntent.setAction(ItemTrackerService.Action.CHECK_ITEMS);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 20000, Application.Keys.CHECK_TRACKED_ITEMS_INTERVAL, PendingIntent.getService(context, 1, serviceIntent, 0));
    }
}
