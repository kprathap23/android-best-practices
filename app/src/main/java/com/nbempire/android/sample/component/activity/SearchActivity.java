package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.task.SearchTask;


public class SearchActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchActivity";
    private static final int ITEMS_PER_PAGE = 15;

    private Search search;
    private EditText query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");

        setContentView(R.layout.activity_search);

        query = (EditText) findViewById(R.id.searchQuery);
        query.setText(getPreferences(MODE_PRIVATE).getString(SearchTask.LAST_QUERY, null));

        search = new Search();
    }

    public void findItems(View view) {
        Log.v(TAG, "findItems...");

        search.setQuery(query.getText().toString());
        search.setPaging(new Paging(ITEMS_PER_PAGE));

        Log.d(TAG, "Starting activity to display search results...");
        Intent resultsIntent = new Intent(this, SearchResultsActivity.class);
        resultsIntent.putExtra(SearchResultsActivity.Keys.SEARCH, search);
        startActivity(resultsIntent);
    }

}
