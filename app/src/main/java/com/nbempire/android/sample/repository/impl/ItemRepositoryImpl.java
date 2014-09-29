package com.nbempire.android.sample.repository.impl;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.repository.ItemRepository;
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
import java.util.ArrayList;
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
            public static final String SUBTITLE = "subtitle";
            public static final String AVAILABLE_QUANTITY = "available_quantity";
            public static final String THUMBNAIL = "thumbnail";
            public static final String INITIAL_QUANTITY = "initial_quantity";
        }

    }

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
        String resource = MainKeys.MELI_API_HOST + "/items/" + id + "?attributes=id,title,price,subtitle,initial_quantity,available_quantity,thumbnail";

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

    private List<Item> parseJsonItems(JSONArray results) throws JSONException {
        List<Item> items = new ArrayList<Item>();

        for (int i = 0; i < results.length(); i++) {
            items.add(parseJsonItem(results.getJSONObject(i), false));
        }

        return items;
    }

    private Item parseJsonItem(JSONObject eachObject, boolean getItemSpecificValues) throws JSONException {
        Item eachItem = new Item(eachObject.getString(Keys.Item.ID), parseJson(eachObject.getString(Keys.Item.TITLE)));
        eachItem.setSubtitle(parseJson(eachObject.getString(Keys.Item.SUBTITLE)));
        eachItem.setAvailableQuantity(parseJson(eachObject.getString(Keys.Item.AVAILABLE_QUANTITY)));
        eachItem.setThumbnail(parseJson(eachObject.getString(Keys.Item.THUMBNAIL)));

        if (getItemSpecificValues) {
            eachItem.setInitialQuantity(parseJson(eachObject.getString(Keys.Item.INITIAL_QUANTITY)));
        }

        return eachItem;
    }

    private static String parseJson(String value) {
        return value == null || value.equalsIgnoreCase("null") ? null : value;
    }
}
