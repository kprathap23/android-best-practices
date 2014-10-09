package com.nbempire.android.sample.repository;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.dto.ItemDto;
import com.nbempire.android.sample.util.impl.PageableImpl;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Developers mus NOT implement this interface. The implementation will be generated on runtime by
 * the Retrofit library. Created by nbarrios on 08/10/14.
 */
public interface ItemRemoteRepository {

    @GET("/items/{id}?attributes=id,title,price,subtitle,initial_quantity,available_quantity,stop_time,pictures")
    ItemDto findById(@Path("id") String id);

    @GET("/sites/MLA/search?attributes=paging,results")
    PageableImpl<Item> findByTitle(@Query("q") String title, @Query("limit") int limit, @Query("offset") int offset);
}
