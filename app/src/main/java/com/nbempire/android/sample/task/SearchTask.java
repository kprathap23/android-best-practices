package com.nbempire.android.sample.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.nbempire.android.sample.MainKeys;
import com.nbempire.android.sample.adapter.ItemAdapter;
import com.nbempire.android.sample.component.fragment.SearchFragment;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchTask extends AsyncTask<Search, Integer, Pageable<Item>> {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchTask";
    public static final String LAST_QUERY = "lastSearchQuery";

    private final Activity context;
    private final SharedPreferences.Editor preferencesEditor;
    private final ItemAdapter itemAdapter;

    private ItemService itemService;

    public SearchTask(Activity context, ItemAdapter itemAdapter) {
        this.context = context;
        this.itemService = ItemServiceImpl.getInstance(context);
        this.preferencesEditor = context.getSharedPreferences(MainKeys.APP_SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        this.itemAdapter = itemAdapter;
    }

    @Override
    protected Pageable<Item> doInBackground(Search... searches) {
        Log.v(TAG, "doInBackground...");
        Search search = searches[0];

        Log.d(TAG, "Storing last search: " + search.getQuery());
        preferencesEditor.putString(LAST_QUERY, search.getQuery()).apply();

        return itemService.find(search);
    }

    @Override
    protected void onPostExecute(Pageable<Item> pageable) {
        context.getIntent().putExtra(SearchFragment.Keys.PAGEABLE, pageable);

        itemAdapter.setPageable(pageable);
        itemAdapter.addAll(pageable.getResult());
        Log.d(TAG, "Total items in adapter: " + itemAdapter.getCount());
    }
}
