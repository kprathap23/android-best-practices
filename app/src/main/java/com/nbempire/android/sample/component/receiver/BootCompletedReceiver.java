package com.nbempire.android.sample.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nbempire.android.sample.component.service.ItemTrackerService;

public class BootCompletedReceiver extends BroadcastReceiver {

    /**
     * Used for log messages.
     */
    private static final String TAG = "BootCompletedReceiver";

    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive...");
        Log.i(TAG, "Starting service: " + ItemTrackerService.class.getName());
        context.startService(new Intent(context, ItemTrackerService.class));
    }
}
