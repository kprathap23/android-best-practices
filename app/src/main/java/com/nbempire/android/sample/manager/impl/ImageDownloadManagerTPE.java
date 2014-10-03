package com.nbempire.android.sample.manager.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        instance.executor.execute(new Runnable() {
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
        });
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
