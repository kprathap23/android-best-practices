package com.nbempire.android.sample.repository.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.dto.ItemDto;
import com.nbempire.android.sample.dto.PictureDto;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.repository.contract.ItemContract;
import com.nbempire.android.sample.repository.dbhelper.ItemsTrackerDbHelper;
import com.nbempire.android.sample.util.Pageable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemRepositoryImpl implements ItemRepository {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemRepositoryImpl";

    private static ItemRepository instance;
    private static ItemRemoteRepository remoteRepository;

    /**
     * Existent keys on API.
     */
    private static class Keys {

        private static class Item {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String PRICE = "price";
            public static final String SUBTITLE = "subtitle";
            public static final String AVAILABLE_QUANTITY = "available_quantity";
            public static final String THUMBNAIL = "thumbnail";
            public static final String STOP_TIME = "stop_time";
        }

    }

    public static ItemRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ItemRepositoryImpl(context);
        }
        return instance;
    }

    private ItemRepositoryImpl(Context context) {
        dbHelper = new ItemsTrackerDbHelper(context);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(MainKeys.MELI_API_HOST)
                .setConverter(new GsonConverter(gson))
                .build();

        remoteRepository = restAdapter.create(ItemRemoteRepository.class);
    }

    private ItemsTrackerDbHelper dbHelper;

    @Override
    public Pageable<Item> findByTitle(String title, Paging paging) {
        Log.d(TAG, "Executing request against MeLi API...");

        Pageable<Item> pageable = remoteRepository.findByTitle(title, paging.getLimit(), paging.getOffset());
        pageable.setQuery(title);

        return pageable;
    }

    @Override
    public Item findById(String id) {
        return parse(remoteRepository.findById(id));
    }

    @Override
    public List<Item> getTrackedItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.Column.ID,
                ItemContract.ItemEntry.Column.TITLE,
                ItemContract.ItemEntry.Column.PRICE,
                ItemContract.ItemEntry.Column.STOP_TIME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = ItemContract.ItemEntry.Column.TITLE + " ASC";

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME,  // The table to query
                projection,                         // The columns to return
                null,                               // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                               // Don't group the rows
                null,                               // Don't filter by row groups
                sortOrder                           // The sort order
        );

        List<Item> items = new ArrayList<Item>();
        if (cursor.getCount() < 0) {
            Log.d(TAG, "Database has no items for tracking.");
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.Column.ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.Column.TITLE));
                Long price = cursor.getLong(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.Column.PRICE));
                Date stopTime = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.Column.STOP_TIME)));

                items.add(new Item(id, title, price, stopTime));
            }
        }

        return items;
    }

    @Override
    public void save(String id, Long price, Long stopTime, String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.Column.ID, id);
        values.put(ItemContract.ItemEntry.Column.TITLE, title);
        values.put(ItemContract.ItemEntry.Column.PRICE, price);
        values.put(ItemContract.ItemEntry.Column.STOP_TIME, stopTime);

        long result = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
        if (result != -1) {
            Log.i(TAG, "Item stored: " + id);
        } else {
            Log.e(TAG, "An error ocurred while storing item: " + id);
        }
    }

    @Override
    public void remove(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = ItemContract.ItemEntry.Column.ID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};

        // Issue SQL statement.
        int affectedRows = db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "Removed rows: " + affectedRows);
    }

    private List<Item> parseJsonItems(JSONArray results) throws JSONException {
        List<Item> items = new ArrayList<Item>();

        for (int i = 0; i < results.length(); i++) {
            items.add(parseJsonItem(results.getJSONObject(i)));
        }

        return items;
    }

    private Item parseJsonItem(JSONObject eachObject) throws JSONException {
        Item eachItem = new Item(eachObject.getString(Keys.Item.ID), parseJson(eachObject.getString(Keys.Item.TITLE)), eachObject.getLong(Keys.Item.PRICE));
        eachItem.setSubtitle(parseJson(eachObject.getString(Keys.Item.SUBTITLE)));
        eachItem.setAvailableQuantity(eachObject.getInt(Keys.Item.AVAILABLE_QUANTITY));

        try {
            eachItem.setThumbnail(parseJson(eachObject.getString(Keys.Item.THUMBNAIL)));
        } catch (JSONException e) {
            // Do nothing. It can be or not.
        }

        try {
            eachItem.setStopTime(parseDate(eachObject.getString(Keys.Item.STOP_TIME)));
        } catch (JSONException e) {
            // Do nothing. It can be or not.
        }

        return eachItem;
    }

    private Date parseDate(String string) {
        Date date = null;
        try {
            String[] partials = string.split("T");
            String value = partials[0] + partials[1];
            date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse(value.substring(0, value.indexOf('.')));
        } catch (ParseException e) {
            Log.e(TAG, "Error while parsing stopTime: " + e.getMessage());
        }
        return date;
    }

    private static String parseJson(String value) {
        return value == null || value.equalsIgnoreCase("null") ? null : value;
    }

    private Item parse(ItemDto itemDto) {
        Item item = new Item(itemDto.getId(), itemDto.getTitle(), itemDto.getPrice(), itemDto.getStopTime());
        item.setAvailableQuantity(itemDto.getAvailableQuantity());
        item.setInitialQuantity(itemDto.getInitialQuantity());
        item.setSubtitle(itemDto.getSubtitle());
        item.setMainPictureUrl(getMainPicture(itemDto.getPictures()));
        return item;
    }

    private String getMainPicture(List<PictureDto> jsonPictures) {
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
