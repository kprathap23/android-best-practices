package com.nbempire.android.sample.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.ItemRemoteRepository;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.util.Pageable;
import com.nbempire.android.sample.vo.ItemVo;
import com.nbempire.android.sample.vo.PictureVo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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

    private static ItemServiceImpl instance;

    private Context context;
    private ItemRepository itemRepository;
    private ItemRemoteRepository itemRemoteRepository;

    public static ItemService getInstance(Context context) {
        if (instance == null) {
            instance = new ItemServiceImpl(context, ItemRepositoryImpl.getInstance(context));
        }
        return instance;
    }

    private ItemServiceImpl(Context context, ItemRepository itemRepository) {
        this.context = context;
        this.itemRepository = itemRepository;

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainKeys.MELI_API_HOST)
                .setConverter(new GsonConverter(gson))
                .build();

        this.itemRemoteRepository = restAdapter.create(ItemRemoteRepository.class);
    }

    @Override
    public Pageable<Item> find(Search search) {
        return itemRepository.findByTitle(search.getQuery(), search.getPaging());
    }

    @Override
    public Item findById(String id) {
        return parse(itemRemoteRepository.findById(id));
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
        ItemVo remoteItem = itemRemoteRepository.findById(item.getId());
        return remoteItem.getPrice() != item.getPrice();
    }

    private Item parse(ItemVo itemVo) {
        Item item = new Item(itemVo.getId(), itemVo.getTitle(), itemVo.getPrice(), itemVo.getStopTime());
        item.setAvailableQuantity(itemVo.getAvailableQuantity());
        item.setInitialQuantity(itemVo.getInitialQuantity());
        item.setSubtitle(itemVo.getSubtitle());
        item.setMainPictureUrl(getMainPicture(itemVo.getPictures()));
        return item;
    }

    private String getMainPicture(List<PictureVo> jsonPictures) {
        String url = null;
        int maxSize = 0;

        for (PictureVo pictureVo : jsonPictures) {
            String[] sizes = pictureVo.getSize().split("x");
            int surface = Integer.valueOf(sizes[0]) * Integer.valueOf(sizes[1]);
            if (surface > maxSize) {
                maxSize = surface;

                url = pictureVo.getUrl();
            }
        }

        return url;
    }

}
