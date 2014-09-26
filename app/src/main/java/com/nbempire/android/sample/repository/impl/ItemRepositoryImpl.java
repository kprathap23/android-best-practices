package com.nbempire.android.sample.repository.impl;

import android.net.http.AndroidHttpClient;
import android.util.Log;

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
        }

        private static class Item {
            public static final String TITLE = "title";
            public static final String SUBTITLE = "subtitle";
            public static final String AVAILABLE_QUANTITY = "available_quantity";
            public static final String THUMBNAIL = "thumbnail";
        }

    }

    @Override
    public Pageable<Item> findByTitle(String title, Paging paging) {
        Pageable<Item> pageable = null;

        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("prueba");
        try {
            String encoding = "UTF-8";
            HttpGet get = new HttpGet("https://api.mercadolibre.com/sites/MLA/search?q=" + URLEncoder.encode(title, encoding) + "&limit=" + paging.getLimit());

            Log.d(TAG, "Executing request against MeLi API...");
            HttpResponse response = androidHttpClient.execute(get);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), encoding));
            JSONObject object = new JSONObject(reader.readLine());

            androidHttpClient.close();

            List<Item> items = parseJsonItems(object.getJSONArray(Keys.Search.RESULTS));

            JSONObject jsonPaging = object.getJSONObject(Keys.Search.PAGING);
            paging.setOffset(jsonPaging.getInt(Keys.Paging.OFFSET));
            paging.setTotal(jsonPaging.getInt(Keys.Paging.TOTAL));

            pageable = new PageableImpl<Item>(items, paging);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding user input for make an HTTP request: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error executing GET: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
        }

        return pageable;
    }

    private List<Item> parseJsonItems(JSONArray results) throws JSONException {
        List<Item> items = new ArrayList<Item>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject eachObject = results.getJSONObject(i);

            Item eachItem = new Item(parseJson(eachObject.getString(Keys.Item.TITLE)));
            eachItem.setSubtitle(parseJson(eachObject.getString(Keys.Item.SUBTITLE)));
            eachItem.setAvailableQuantity(parseJson(eachObject.getString(Keys.Item.AVAILABLE_QUANTITY)));
            eachItem.setThumbnail(parseJson(eachObject.getString(Keys.Item.THUMBNAIL)));

            items.add(eachItem);
        }

        return items;
    }

    private static String parseJson(String value) {
        return value == null || value.equalsIgnoreCase("null") ? null : value;
    }
}
