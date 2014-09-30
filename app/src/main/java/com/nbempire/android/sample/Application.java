package com.nbempire.android.sample;

import android.util.Log;

import com.nbempire.android.sample.component.receiver.BootCompletedReceiver;

/**
 * Created by nbarrios on 30/09/14.
 */
public class Application extends android.app.Application {

    /**
     * Used for log messages.
     */
    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate...");

        BootCompletedReceiver.setupApplicationIfNeeded(this);
    }
}
