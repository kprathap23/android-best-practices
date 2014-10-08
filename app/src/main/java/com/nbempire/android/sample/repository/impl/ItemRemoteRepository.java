package com.nbempire.android.sample.repository.impl;

import com.nbempire.android.sample.vo.ItemVo;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Developers mus NOT implement this interface. The implementation will be generated on runtime by
 * the Retrofit library. Created by nbarrios on 08/10/14.
 */
public interface ItemRemoteRepository {

    @GET("/items/{id}?attributes=id,title,price,subtitle,initial_quantity,available_quantity,stop_time,pictures")
    ItemVo findById(@Path("id") String id);

}
