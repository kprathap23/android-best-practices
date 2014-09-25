package com.nbempire.android.sample.manager.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.nbempire.android.sample.manager.ImageDownloadManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by nbarrios on 25/09/14.
 */
public class ImageDownloadManagerImpl implements ImageDownloadManager {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ImageDownloadManagerImpl";

    private ImageDownloadManagerImpl() {
    }

    @Override
    public void load(final String uri, final ImageView imageView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = loadImageFromNetwork(uri);

                imageView.post(new Runnable() {
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

            }
        }).start();
    }

    private Bitmap loadImageFromNetwork(String uri) {
        InputStream inputStream = null;

        try {
            inputStream = new URL(uri).openStream();
        } catch (IOException e) {
            Log.e(TAG, "Error while getting image: " + e.getMessage());
        }

        return BitmapFactory.decodeStream(inputStream);
    }

    public static ImageDownloadManagerImpl getInstance() {
        return new ImageDownloadManagerImpl();
    }
}
