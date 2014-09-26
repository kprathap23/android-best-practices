package com.nbempire.android.sample.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.adapter.ItemAdapter;
import com.nbempire.android.sample.component.activity.SearchResultsActivity;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
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
    private final ListView resultsListView;

    private ItemService itemService;

    public SearchTask(Activity context) {
        this.context = context;
        this.itemService = new ItemServiceImpl(new ItemRepositoryImpl());
        this.preferencesEditor = context.getPreferences(Context.MODE_PRIVATE).edit();

        resultsListView = (ListView) context.findViewById(R.id.searchResultsListView);
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
        context.getIntent().putExtra(SearchResultsActivity.Keys.PAGEABLE, pageable);

        ItemAdapter adapter = (ItemAdapter) resultsListView.getAdapter();
        if (adapter == null) {
            resultsListView.setAdapter(new ItemAdapter(context, pageable.getResult()));
        } else {
            adapter.clear();
            adapter.addAll(pageable.getResult());
        }
    }
}
