package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.adapter.ItemAdapter;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.task.SearchTask;
import com.nbempire.android.sample.util.Pageable;

import java.util.ArrayList;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchResultsActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchResultsActivity";
    private ItemAdapter itemAdapter;

    ListView resultsListView;

    public class Keys {
        public static final String SEARCH = "search";
        public static final String PAGEABLE = "pageable";
        public static final String ITEM_ADAPTER = "itemAdapter";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_search_results);

        resultsListView = (ListView) findViewById(R.id.searchResultsListView);

        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(this);
        }
        resultsListView.setAdapter(itemAdapter);

        if (savedInstanceState == null) {
            Search search = getIntent().getParcelableExtra(Keys.SEARCH);
            Log.d(TAG, "Finding items for query: " + search.getQuery());
            new SearchTask(this).execute(search);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState...");
        outState.putBundle(Keys.ITEM_ADAPTER, itemAdapter.getState());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState...");

        Bundle itemAdapterState = savedInstanceState.getBundle(Keys.ITEM_ADAPTER);
        ArrayList<Item> results = itemAdapterState.getParcelableArrayList(ItemAdapter.Keys.RESULTS);
        itemAdapter.addAll(results);
        itemAdapter.setLoadedPages(itemAdapterState.getIntArray(ItemAdapter.Keys.LOADED_PAGES_KEYS), itemAdapterState.getBooleanArray(ItemAdapter.Keys.LOADED_PAGES_VALUES));
        itemAdapter.setPageable((Pageable<Item>) itemAdapterState.getParcelable(ItemAdapter.Keys.PAGEABLE));
        Log.d(TAG, "Total items in adapter: " + itemAdapter.getCount());
    }
}
