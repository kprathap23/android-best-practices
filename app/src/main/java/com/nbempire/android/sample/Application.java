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

    /**
     * Created by nbarrios on 26/09/14.
     */
    public static final class Keys {
        public static final String MELI_API_HOST = "https://api.mercadolibre.com";

        public static final String APP_SHARED_PREFERENCES = "app";

        public static final int CHECK_TRACKED_ITEMS_INTERVAL = 60000;
        public static final String CHECK_TRACKED_ITEMS_INTERVAL_SECONDS = "60";

        public static final String TRACKED_ITEMS_ID = "trackedItemsId";
    }
}
