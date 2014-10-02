package com.nbempire.android.sample.manager.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.manager.ImageDownloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbarrios on 25/09/14.
 * <p/>
 * Some parts of the code was taken from the official <a href="http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html">Android
 * documentation</a>
 */
public class ImageDownloadManagerImpl implements ImageDownloadManager {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ImageDownloadManagerImpl";

    private static ImageDownloadManagerImpl instance;

    private static List<Thread> threads;

    private LruCache<String, Bitmap> memoryCache;

    private ImageDownloadManagerImpl() {
        threads = new ArrayList<Thread>();

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

    @Override
    public void load(final String uri, final ImageView imageView) {
        final Bitmap bitmap = memoryCache.get(uri);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Log.d(TAG, "Resource not available in memory cache. Creating new thread to get it from the network.");

            queueRunnable(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = loadBitmapFromNetwork(uri);

                    imageView.post(new Runnable() {
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                    if (memoryCache.get(uri) == null) {
                        memoryCache.put(uri, bitmap);
                    }

                }
            });
        }
    }

    private void queueRunnable(Runnable runnable) {
        if (threads.size() < MainKeys.MAX_THREADS) {
            createThread(runnable);
        } else {
            Thread threadToRemove = null;
            for (Thread eachThread : threads) {
                if (!eachThread.isAlive()) {
                    threadToRemove = eachThread;
                    break;
                }
            }

            if (threadToRemove != null) {
                threads.remove(threadToRemove);
                Log.d(TAG, "Thread pool has: " + threads.size() + " of: " + MainKeys.MAX_THREADS);
                createThread(runnable);
            } else {
                Log.d(TAG, "Thread pool is full, queueing work waiting for a free thread.");
                //  TODO : Do magic
            }
        }
    }

    private void createThread(Runnable runnable) {
        Log.v(TAG, "createThread...");

        Thread thread = new Thread(runnable);
        thread.start();
        threads.add(thread);

        Log.d(TAG, "Thread pool has: " + threads.size() + " of: " + MainKeys.MAX_THREADS);
    }

    private Bitmap loadBitmapFromNetwork(String uri) {
        InputStream inputStream = null;

        try {
            inputStream = new URL(uri).openStream();
        } catch (IOException e) {
            Log.e(TAG, "Error while getting image: " + e.getMessage());
        }

        return BitmapFactory.decodeStream(inputStream);
    }

    public static ImageDownloadManagerImpl getInstance() {
        if (instance == null) {
            instance = new ImageDownloadManagerImpl();
        }
        return instance;
    }
}
