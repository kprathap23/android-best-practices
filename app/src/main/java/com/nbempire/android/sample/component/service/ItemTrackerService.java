package com.nbempire.android.sample.component.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a
 * separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static helper methods.
 */
public class ItemTrackerService extends IntentService {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemTrackerService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand...");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind...");
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy...");
        super.onDestroy();
    }

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CHECK_ITEMS = "com.nbempire.android.sample.component.service.action.CHECK_ITEMS";
    private static final String ACTION_FOO = "com.nbempire.android.sample.component.service.action.FOO";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.nbempire.android.sample.component.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.nbempire.android.sample.component.service.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If the service is
     * already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ItemTrackerService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public ItemTrackerService() {
        super("ItemTrackerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent...");

        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_CHECK_ITEMS.equals(action)) {
                handleActionCheckItems();
            } else if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            }
        }
    }

    private void handleActionCheckItems() {
        Log.v(TAG, "handleActionCheckItems...");
    }

    /**
     * Handle action Foo in the provided background thread with the provided parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
