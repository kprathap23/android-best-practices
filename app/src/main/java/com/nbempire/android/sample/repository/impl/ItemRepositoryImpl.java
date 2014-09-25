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
            public static final String RESULTADOS = "results";
        }

        private static class Item {
            public static final String TITULO = "title";
            public static final String SUBTITULO = "subtitle";
            public static final String CANTIDAD_DISPONIBLE = "available_quantity";
            public static final String THUMBNAIL = "thumbnail";
        }

    }

    @Override
    public List<Item> findByTitulo(String titulo) {
        List<Item> items = new ArrayList<Item>();

        AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("prueba");
        HttpGet get = null;
        try {
            get = new HttpGet("https://api.mercadolibre.com/sites/MLA/search?q=" + URLEncoder.encode(titulo, "UTF-8") + "&limit=30");

            Log.d(TAG, "Executing request against MeLi API...");
            HttpResponse response = androidHttpClient.execute(get);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            JSONObject object = new JSONObject(reader.readLine());

            androidHttpClient.close();

            items = parseJsonItems(object.getJSONArray(Keys.Search.RESULTADOS));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding user input for make an HTTP request: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error executing GET: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
        }

        return items;
    }

    private List<Item> parseJsonItems(JSONArray results) throws JSONException {
        List<Item> items = new ArrayList<Item>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject eachObject = results.getJSONObject(i);

            Item eachItem = new Item(parseJson(eachObject.getString(Keys.Item.TITULO)));
            eachItem.setSubtitulo(parseJson(eachObject.getString(Keys.Item.SUBTITULO)));
            eachItem.setCantidadDisponible(parseJson(eachObject.getString(Keys.Item.CANTIDAD_DISPONIBLE)));
            eachItem.setThumbnail(parseJson(eachObject.getString(Keys.Item.THUMBNAIL)));

            items.add(eachItem);
        }

        return items;
    }

    private static String parseJson(String value) {
        return value == null || value.equalsIgnoreCase("null") ? null : value;
    }
}
