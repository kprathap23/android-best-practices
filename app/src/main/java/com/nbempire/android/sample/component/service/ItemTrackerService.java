package com.nbempire.android.sample.component.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.component.activity.VIPActivity;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.nbempire.android.sample.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in a service on a
 * separate handler thread.
 */
public class ItemTrackerService extends IntentService {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemTrackerService";

    private ItemService itemService;

    public static class Action {
        public static final String CHECK_ITEMS = "com.nbempire.android.sample.component.service.action.CHECK_ITEMS";
        private static final String TRACK_ITEM = "com.nbempire.android.sample.component.service.action.TRACK_ITEM";
        private static final String STOP_TRACKING_ITEM = "com.nbempire.android.sample.component.service.action.STOP_TRACKING_ITEM";
    }

    private static class Keys {
        private static final String ITEM_ID = "itemId";
        private static final String ITEM_TITLE = "itemTitle";
        private static final String ITEM_PRICE = "itemPrice";
        private static final String ITEM_STOP_TIME = "itemStopTime";
    }

    public ItemTrackerService() {
        super("ItemTrackerService");
        itemService = ItemServiceImpl.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent...");

        if (intent != null) {
            final String action = intent.getAction();

            if (Action.CHECK_ITEMS.equals(action)) {
                handleActionCheckItems();
            } else if (Action.TRACK_ITEM.equals(action)) {
                itemService.trackItem(intent.getStringExtra(Keys.ITEM_ID), intent.getLongExtra(Keys.ITEM_PRICE, 0), intent.getLongExtra(Keys.ITEM_STOP_TIME, 0), intent.getStringExtra(Keys.ITEM_TITLE));
            } else if (Action.STOP_TRACKING_ITEM.equals(action)) {
                itemService.stopTracking(intent.getStringExtra(Keys.ITEM_ID));
            } else {
                Log.e(TAG, "No action mapped for value: " + action);
                //  TODO : Should I do something here?
            }
        }
    }

    private void handleActionCheckItems() {
        Log.v(TAG, "handleActionCheckItems...");
        List<Item> trackedItems = itemService.getTrackedItems();

        for (Item trackedItem : trackedItems) {
            Log.d(TAG, "Checking item id: " + trackedItem.getId() + ", price: " + trackedItem.getPrice() + ", stopTime: " + trackedItem.getStopTime());

            if (DateUtils.nearTo(trackedItem.getStopTime(), 5)) {
                Log.d(TAG, "Item stop time is near.");

                displayNotification(trackedItem, getString(R.string.an_interesting_item_near_to_finish));

                if (DateUtils.isToday(trackedItem.getStopTime())) {
                    itemService.stopTracking(trackedItem.getId());
                }
            } else {
                Log.d(TAG, "Checking if the item was recently updated.");

                if (itemService.checkTrackedItem(trackedItem)) {
                    displayNotification(trackedItem, getString(R.string.an_interesting_item_has_been_updated));
                } else {
                    Log.d(TAG, "Item: " + trackedItem.getId() + " will not generate a user notification.");
                }
            }
        }

        if (trackedItems.size() == 0) {
            Log.d(TAG, "There are no tracked items in database.");
        }
    }

    private void displayNotification(Item item, String contentText) {
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(item.getTitle())
                        .setContentText(contentText)
                        .setAutoCancel(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, VIPActivity.class);
        resultIntent.putExtra(VIPActivity.Keys.ITEM, item);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(VIPActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // id allows you to update the notification later on.
        mNotificationManager.notify(item.getId().hashCode(), mBuilder.build());
    }

    //  TODO : Refactor public methods...

    /**
     * Starts this service to perform action track item with the given parameters. If the service is
     * already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionTrackItem(Context context, String id, String title, float price, Date stopTime) {
        Intent intent = new Intent(context, ItemTrackerService.class);
        intent.setAction(Action.TRACK_ITEM);
        intent.putExtra(Keys.ITEM_ID, id);
        intent.putExtra(Keys.ITEM_TITLE, title);
        intent.putExtra(Keys.ITEM_PRICE, price);
        intent.putExtra(Keys.ITEM_STOP_TIME, stopTime.getTime());
        context.startService(intent);
    }

    /**
     * Starts this service to perform action stop tracking item with the given parameters. If the
     * service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionStopTrackingItem(Activity context, String id) {
        Intent intent = new Intent(context, ItemTrackerService.class);
        intent.setAction(Action.STOP_TRACKING_ITEM);
        intent.putExtra(Keys.ITEM_ID, id);
        context.startService(intent);
    }

}
