package com.nbempire.android.sample.manager;

import android.widget.ImageView;

/**
 * Created by nbarrios on 25/09/14.
 */
public interface ImageDownloadManager {

    /**
     * It must do network operations from a new thread.
     * <p/>
     * It save all resources in a memory cache.
     *
     * @param uri       The resource to load.
     * @param imageView The {@link android.widget.ImageView} object where to load the image once it was loaded.
     */
    void load(String uri, ImageView imageView);

}
