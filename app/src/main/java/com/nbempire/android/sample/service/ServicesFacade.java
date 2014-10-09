package com.nbempire.android.sample.service;

import android.content.Context;

import com.nbempire.android.sample.service.impl.ItemServiceImpl;

/**
 * Created by nbarrios on 09/10/14.
 */
public abstract class ServicesFacade {

    public static ItemService getItemService(Context context) {
        return ItemServiceImpl.getInstance(context);
    }
}
