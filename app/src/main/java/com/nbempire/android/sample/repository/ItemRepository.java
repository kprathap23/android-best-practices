package com.nbempire.android.sample.repository;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.util.Pageable;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemRepository {

    Pageable<Item> findByTitle(String title, Paging paging);

    List<Item> getTrackedItems();

    /**
     * Save this item into local storage.
     *
     * @param id
     * @param price
     * @param stopTime
     * @param title
     */
    void save(String id, Long price, Long stopTime, String title);

    /**
     * Remove this item from local storage.
     *
     * @param id
     */
    void remove(String id);
}
