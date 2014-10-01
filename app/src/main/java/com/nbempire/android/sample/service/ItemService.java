package com.nbempire.android.sample.service;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.util.Pageable;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemService {

    Pageable<Item> find(Search search);

    Item findById(String id);

    List<Item> getTrackedItems();

    void trackItem(String id, Long price, Long stopTime, String title);

    boolean isTracked(String id);

    void stopTracking(String id);

    boolean checkTrackedItem(Item item);
}
