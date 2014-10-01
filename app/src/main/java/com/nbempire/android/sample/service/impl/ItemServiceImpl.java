package com.nbempire.android.sample.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.util.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nbempire.android.sample.MainKeys.APP_SHARED_PREFERENCES;
import static com.nbempire.android.sample.MainKeys.TRACKED_ITEMS_ID;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemServiceImpl implements ItemService {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemServiceImpl";

    private Context context;
    private ItemRepository itemRepository;

    public ItemServiceImpl(Context context, ItemRepository itemRepository) {
        this.context = context;
        this.itemRepository = itemRepository;
    }

    @Override
    public Pageable<Item> find(Search search) {
        return itemRepository.findByTitle(search.getQuery(), search.getPaging());
    }

    @Override
    public Item findById(String id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> getTrackedItems() {
        return itemRepository.getTrackedItems();
    }

    @Override
    public void trackItem(String id, Long price, Long stopTime, String title) {
        Log.v(TAG, "trackItem... id: " + id + ", price: " + price + ", milliseconds: " + stopTime + ", title: " + title);
        itemRepository.save(id, price, stopTime, title);

        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> ids = preferences.getStringSet(MainKeys.TRACKED_ITEMS_ID, new HashSet<String>());
        ids.add(id);
        preferences.edit().putStringSet(TRACKED_ITEMS_ID, ids).apply();
    }

    @Override
    public boolean isTracked(String id) {
        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getStringSet(MainKeys.TRACKED_ITEMS_ID, new HashSet<String>()).contains(id);
    }

    @Override
    public void stopTracking(String id) {
        Log.v(TAG, "stopTracking... id: " + id);

        // Remove item from shared preferences "cache".
        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> ids = preferences.getStringSet(MainKeys.TRACKED_ITEMS_ID, new HashSet<String>());
        ids.remove(id);
        preferences.edit().putStringSet(TRACKED_ITEMS_ID, ids).apply();

        // Remove item from local storage too.
        itemRepository.remove(id);
    }

    @Override
    public boolean checkTrackedItem(Item item) {
        Item remoteItem = itemRepository.findById(item.getId());

        return remoteItem.getPrice() != item.getPrice();
    }

}
