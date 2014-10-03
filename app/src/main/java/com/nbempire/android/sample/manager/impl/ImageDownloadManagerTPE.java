package com.nbempire.android.sample.manager.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
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

    private static LruCache<String, Bitmap> memoryCache;

    private ImageDownloadManagerTPE() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
        executor = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 5, TimeUnit.SECONDS, queue);
        createMemoryCache();
    }

    /**
     * Loads an image from the given uri either from the cache or the network.
     * <p/>
     * It save all resources in a memory cache.
     *
     * @param uri       The resource to load.
     * @param imageView The {@link android.widget.ImageView} object where to load the image once it
     *                  was loaded.
     */
    public static void load(final String uri, final ImageView imageView) {
        Log.v(TAG, "load..." + "uri: " + uri);

        final Bitmap bitmap = memoryCache.get(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "Resource loaded from memory cache");
        } else {
            Log.d(TAG, "Resource not available in memory cache. Queueing task to the image manager...");

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
                    if (bitmap != null) {
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });

                        memoryCache.put(uri, bitmap);
                    }
                }

                @Override
                public int getId() {
                    return this.id;
                }
            });
        }
    }

    /**
     * Removes from the executor a task that is no longer needed because of, for instance, the user
     * has scrolled down to much and we queued too many images.
     *
     * @param runnableId The id of a Runnable to look for.
     */
    private static void removePendingTask(int runnableId) {
        Log.v(TAG, "removePendingTask...");
        Log.d(TAG, "Searching for runnable with id: " + runnableId);

        List<Runnable> toRemove = new ArrayList<Runnable>();
        for (Runnable runnable : instance.executor.getQueue()) {
            if (((CustomRunnable) runnable).getId() == runnableId) {
                toRemove.add(runnable);
            }
        }

        for (Runnable runnable : toRemove) {
            Log.d(TAG, "Removing Runnable " + ((CustomRunnable) runnable).getId() + " from queue");
            instance.executor.remove(runnable);
        }

        Log.d(TAG, "Removed: " + toRemove.size() + " runnables from queue");
    }

    private static Bitmap loadBitmapFromNetwork(String uri) {
        Log.v(TAG, "loadBitmapFromNetwork...");

        InputStream inputStream = null;
        try {
            inputStream = new URL(uri).openStream();
        } catch (IOException e) {
            Log.e(TAG, "Error while getting image: " + e.getMessage());
        }

        return BitmapFactory.decodeStream(inputStream);
    }

    private void createMemoryCache() {
        // Creates a memory cache.
        // In this example, one eighth of the application memory is allocated for our cache. On a normal/hdpi device this is a minimum of
        // around 4MB (32/8). A full screen GridView filled with images on a device with 800x480 resolution would use around 1.5MB (800*480*4 bytes),
        // so this would cache a minimum of around 2.5 pages of images in memory.
        // Get max available VM memory, exceeding this amount will throw an OutOfMemory exception. Stored in kilobytes as LruCache takes an int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        Log.d(TAG, "Memory cache created.");
    }

}
