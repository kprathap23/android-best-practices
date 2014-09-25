package com.nbempire.android.sample.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import com.nbempire.android.sample.component.activity.SearchResultsActivity;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchTask extends AsyncTask<Search, Integer, List<Item>> {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchTask";
    private final Activity context;
    private ItemService itemService;

    public SearchTask(Activity context) {
        this.context = context;
        this.itemService = new ItemServiceImpl(new ItemRepositoryImpl());
    }

    @Override
    protected List<Item> doInBackground(Search... searches) {
        return itemService.find(searches[0]);
    }

    @Override
    protected void onPostExecute(List<Item> items) {
        Log.d(TAG, "Starting activity to display search results...");

        Intent resultsIntent = new Intent(context, SearchResultsActivity.class);
        resultsIntent.putParcelableArrayListExtra(SearchResultsActivity.Keys.RESULTS, new ArrayList<Parcelable>(items));
        context.startActivity(resultsIntent);
    }
}
