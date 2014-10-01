package com.nbempire.android.sample.repository.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.repository.contract.ItemContract;
import com.nbempire.android.sample.repository.dbhelper.ItemsTrackerDbHelper;
import com.nbempire.android.sample.util.Pageable;
import com.nbempire.android.sample.util.impl.PageableImpl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemRepositoryImpl implements ItemRepository {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemRepositoryImpl";
    private static final String ENCODING = "UTF-8";
    private static final String HTTP_CLIENT_USER_AGENT = "userAgent";

    /**
     * Existent keys on API.
     */
    private static class Keys {

        private static class Search {
            public static final String RESULTS = "results";
            public static final String PAGING = "paging";
        }

        private static class Paging {
            public static final String TOTAL = "total";
            public static final String OFFSET = "offset";
            public static final String LIMIT = "limit";
        }

        private static class Item {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String PRICE = "price";
            public static final String SUBTITLE = "subtitle";
            public static final String AVAILABLE_QUANTITY = "available_quantity";
            public static final String THUMBNAIL = "thumbnail";
            public static final String INITIAL_QUANTITY = "initial_quantity";
            public static final String PICTURES = "pictures";
            public static final String STOP_TIME = "stop_time";

            public class Picture {
                public static final String SIZE = "size";
                public static final String URL = "url";
            }
        }

    }

    public ItemRepositoryImpl(Context context) {
        dbHelper = new ItemsTrackerDbHelper(context);
    }

    private ItemsTrackerDbHelper dbHelper;

    @Override
    public Pageable<Item> findByTitle(String title, Paging paging) {
        Pageable<Item> pageable = null;

        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance(HTTP_CLIENT_USER_AGENT);
        try {
            String resource = MainKeys.MELI_API_HOST + "/sites/MLA/search?q=" + URLEncoder.encode(title, ENCODING) + "&limit=" + paging.getLimit() + "&offset=" + paging.getOffset() + "&attributes=paging,results";
            HttpGet get = new HttpGet(resource);

            Log.d(TAG, "Executing request against MeLi API: " + resource);
            HttpResponse response = androidHttpClient.execute(get);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), ENCODING));
            JSONObject object = new JSONObject(reader.readLine());

            androidHttpClient.close();

            List<Item> items = parseJsonItems(object.getJSONArray(Keys.Search.RESULTS));

            JSONObject jsonPaging = object.getJSONObject(Keys.Search.PAGING);
            paging.setOffset(jsonPaging.getInt(Keys.Paging.OFFSET));
            paging.setTotal(jsonPaging.getInt(Keys.Paging.TOTAL));
            paging.setLimit(jsonPaging.getInt(Keys.Paging.LIMIT));

            pageable = new PageableImpl<Item>(title, items, paging);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding user input for make an HTTP request: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error executing GET: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
        }

        return pageable;
    }

    @Override
    public Item findById(String id) {
        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance(HTTP_CLIENT_USER_AGENT);
        String resource = MainKeys.MELI_API_HOST + "/items/" + id + "?attributes=id,title,price,subtitle,initial_quantity,available_quantity,stop_time,pictures";

        HttpGet get = new HttpGet(resource);

        Log.d(TAG, "Executing request against MeLi API: " + resource);
        Item item = null;
        try {
            HttpResponse response = androidHttpClient.execute(get);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), ENCODING));
            JSONObject jsonObject = new JSONObject(reader.readLine());

            androidHttpClient.close();

            item = parseJsonItem(jsonObject, true);
        } catch (IOException e) {
            Log.e(TAG, "Error executing GET: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
        }

        return item;
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
            items.add(parseJsonItem(results.getJSONObject(i), false));
        }

        return items;
    }

    private Item parseJsonItem(JSONObject eachObject, boolean getItemSpecificValues) throws JSONException {
        Item eachItem = new Item(eachObject.getString(Keys.Item.ID), parseJson(eachObject.getString(Keys.Item.TITLE)), eachObject.getLong(Keys.Item.PRICE));
        eachItem.setSubtitle(parseJson(eachObject.getString(Keys.Item.SUBTITLE)));
        eachItem.setAvailableQuantity(parseJson(eachObject.getString(Keys.Item.AVAILABLE_QUANTITY)));

        try {
            eachItem.setThumbnail(parseJson(eachObject.getString(Keys.Item.THUMBNAIL)));
            eachItem.setStopTime(parseDate(eachObject.getString(Keys.Item.STOP_TIME)));
        } catch (JSONException e) {
            // Do nothing. It can be or not.
        }


        if (getItemSpecificValues) {
            eachItem.setInitialQuantity(parseJson(eachObject.getString(Keys.Item.INITIAL_QUANTITY)));
            eachItem.setMainPictureUrl(getMainPicture(eachObject.getJSONArray(Keys.Item.PICTURES)));
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

    private String getMainPicture(JSONArray jsonPictures) throws JSONException {
        String url = null;
        int maxSize = 0;


        for (int i = 0; i < jsonPictures.length(); i++) {
            JSONObject eachPicture = jsonPictures.getJSONObject(i);

            String[] sizes = eachPicture.getString(Keys.Item.Picture.SIZE).split("x");
            int surface = Integer.valueOf(sizes[0]) * Integer.valueOf(sizes[1]);
            if (surface > maxSize) {
                maxSize = surface;

                url = eachPicture.getString(Keys.Item.Picture.URL);
            }
        }

        return url;
    }

    private static String parseJson(String value) {
        return value == null || value.equalsIgnoreCase("null") ? null : value;
    }
}
