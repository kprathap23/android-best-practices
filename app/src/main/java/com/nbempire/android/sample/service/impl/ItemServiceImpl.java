package com.nbempire.android.sample.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nbempire.android.sample.Application;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.dto.ItemDto;
import com.nbempire.android.sample.dto.PictureDto;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.repository.RepositoriesFacade;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.util.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nbempire.android.sample.Application.Keys.APP_SHARED_PREFERENCES;
import static com.nbempire.android.sample.Application.Keys.TRACKED_ITEMS_ID;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemServiceImpl implements ItemService {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemServiceImpl";

    private static ItemServiceImpl instance;

    private Context context;
    private ItemRepository itemRepository;

    public static ItemService getInstance(Context context) {
        if (instance == null) {
            instance = new ItemServiceImpl(context, RepositoriesFacade.getItemRepository(context));
        }
        return instance;
    }

    private ItemServiceImpl(Context context, ItemRepository itemRepository) {
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
    public void trackItem(String id, float price, Long stopTime, String title) {
        Log.v(TAG, "trackItem... id: " + id + ", price: " + price + ", milliseconds: " + stopTime + ", title: " + title);
        itemRepository.save(id, price, stopTime, title);

        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> ids = preferences.getStringSet(Application.Keys.TRACKED_ITEMS_ID, new HashSet<String>());
        ids.add(id);
        preferences.edit().putStringSet(TRACKED_ITEMS_ID, ids).apply();
    }

    @Override
    public boolean isTracked(String id) {
        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getStringSet(Application.Keys.TRACKED_ITEMS_ID, new HashSet<String>()).contains(id);
    }

    @Override
    public void stopTracking(String id) {
        Log.v(TAG, "stopTracking... id: " + id);

        // Remove item from shared preferences "cache".
        SharedPreferences preferences = context.getSharedPreferences(APP_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        Set<String> ids = preferences.getStringSet(Application.Keys.TRACKED_ITEMS_ID, new HashSet<String>());
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

    public static Item parse(ItemDto itemDto) {
        Item item = new Item(itemDto.getId(), itemDto.getTitle(), itemDto.getPrice(), itemDto.getStopTime());
        item.setAvailableQuantity(itemDto.getAvailableQuantity());
        item.setInitialQuantity(itemDto.getInitialQuantity());
        item.setSubtitle(itemDto.getSubtitle());
        item.setMainPictureUrl(getMainPicture(itemDto.getPictures()));
        return item;
    }

    private static String getMainPicture(List<PictureDto> jsonPictures) {
        String url = null;
        int maxSize = 0;

        for (PictureDto pictureDto : jsonPictures) {
            String[] sizes = pictureDto.getSize().split("x");
            int surface = Integer.valueOf(sizes[0]) * Integer.valueOf(sizes[1]);
            if (surface > maxSize) {
                maxSize = surface;

                url = pictureDto.getUrl();
            }
        }

        return url;
    }
}
