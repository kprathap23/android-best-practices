package com.nbempire.android.sample.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import com.nbempire.android.sample.component.activity.SearchResultsActivity;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.nbempire.android.sample.util.Pageable;

import java.util.ArrayList;

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

    private ItemService itemService;

    public SearchTask(Activity context) {
        this.context = context;
        this.itemService = new ItemServiceImpl(new ItemRepositoryImpl());
        this.preferencesEditor = context.getPreferences(Context.MODE_PRIVATE).edit();
    }

    @Override
    protected Pageable<Item> doInBackground(Search... searches) {
        Search search = searches[0];

        Log.d(TAG, "Storing last search...");
        preferencesEditor.putString(LAST_QUERY, search.getQuery()).apply();

        return itemService.find(search);
    }

    @Override
    protected void onPostExecute(Pageable<Item> pageable) {
        Log.d(TAG, "Starting activity to display search results...");

        Intent resultsIntent = new Intent(context, SearchResultsActivity.class);
        resultsIntent.putParcelableArrayListExtra(SearchResultsActivity.Keys.RESULTS, new ArrayList<Parcelable>(pageable.getResult()));
        context.startActivity(resultsIntent);
    }
}
