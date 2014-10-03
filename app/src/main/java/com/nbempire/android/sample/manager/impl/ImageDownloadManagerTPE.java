package com.nbempire.android.sample.manager.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.nbempire.android.sample.util.CustomRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by nbarrios on 02/10/14.
 */
public class ImageDownloadManagerTPE {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ImageDownloadManagerTPE";

    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final ImageDownloadManagerTPE instance;

    static {
        instance = new ImageDownloadManagerTPE();
    }

    private ThreadPoolExecutor executor;

    private ImageDownloadManagerTPE() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        executor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 5, TimeUnit.SECONDS, queue);
    }

    public static void load(final String uri, final ImageView imageView) {
        Log.v(TAG, "load...");

        if (imageView.getTag() != null) {
            removePendingTask((Integer) imageView.getTag());
        }

        final int newId = uri.hashCode();
        imageView.setTag(newId);

        instance.executor.execute(new CustomRunnable() {
            private int id = newId;

            @Override
            public void run() {
                final Bitmap bitmap = loadBitmapFromNetwork(uri);

                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public int getId() {
                return this.id;
            }
        });
    }

    /**
     * Removes from the executor a task that is no longer needed because of, for instance, the user
     * has scrolled down to much and we queued too many images.
     *
     * @param runnableId The id of a Runnable to look for.
     */
    private static void removePendingTask(int runnableId) {
        Log.d(TAG, "Queue size: " + instance.executor.getQueue().size());
        Log.d(TAG, "Searching for a Runnable with id: " + runnableId);

        List<Runnable> toRemove = new ArrayList<Runnable>();
        for (Runnable runnable : instance.executor.getQueue()) {
            Log.d(TAG, "Runnable id: " + runnableId + ", eachId: " + ((CustomRunnable) runnable).getId());
            if (((CustomRunnable) runnable).getId() == runnableId) {
                toRemove.add(runnable);
            }
        }

        Log.d(TAG, "Added " + toRemove.size() + " to remove from the original: " + instance.executor.getQueue().size());

        for (Runnable runnable : toRemove) {
            Log.v(TAG, "Removing Runnable " + ((CustomRunnable) runnable).getId() + " from executor's queue");
            instance.executor.remove(runnable);
        }

        Log.d(TAG, "After remove: " + instance.executor.getQueue().size());
    }

    private static Bitmap loadBitmapFromNetwork(String uri) {
        InputStream inputStream = null;

        try {
            inputStream = new URL(uri).openStream();
        } catch (IOException e) {
            Log.e(TAG, "Error while getting image: " + e.getMessage());
        }

        return BitmapFactory.decodeStream(inputStream);
    }

}
