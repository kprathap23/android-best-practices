package com.nbempire.android.sample.repository.impl;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.repository.ItemRepository;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemRepositoryImpl implements ItemRepository {

    private static final String TAG = "ItemRepositoryImpl";

    /**
     * Existent keys on API.
     */
    private static class Keys {

        private static class Search {
            public static final String RESULTS = "results";
        }

        private static class Item {
            public static final String TITLE = "title";
        }

    }

    @Override
    public List<Item> findByTitulo(String titulo) {
        List<Item> items = new ArrayList<Item>();

        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("prueba");
        HttpGet get = new HttpGet("https://api.mercadolibre.com/sites/MLA/search?q=" + titulo + "&limit=30");
        try {
            HttpResponse response = androidHttpClient.execute(get);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null; ) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());

            androidHttpClient.close();

            JSONObject object = new JSONObject(tokener);
            Log.d(TAG, "resultado: " + object.toString());
            String query = object.getString("query");

            Log.d(TAG, "query vale: " + query);
            Log.d(TAG, "site_id vale: " + object.getString("site_id"));

            JSONArray results = object.getJSONArray(Keys.Search.RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject eachObject = results.getJSONObject(i);
                items.add(new Item(eachObject.getString(Keys.Item.TITLE)));
            }
        } catch (IOException e) {
            Log.e(TAG, "Error executing GET: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
        }

        return items;
    }
}
