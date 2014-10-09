package com.nbempire.android.sample.component.service;

import android.util.Log;

import com.nbempire.android.sample.Application;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by nbarrios on 09/10/14.
 */
public class RetrofitSpiceService extends RetrofitGsonSpiceService {

    /**
     * Used for log messages.
     */
    private static final String TAG = "RetrofitSpiceService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate...");

        //  TODO : Works even if it is commented.
//        addRetrofitInterface(ItemRemoteRepository.class);
    }

    @Override
    protected String getServerUrl() {
        Log.v(TAG, "getServerUrl...");
        return Application.Keys.MELI_API_HOST;
    }

}
