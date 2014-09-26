package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.task.SearchTask;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchResultsActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchResultsActivity";

    public class Keys {
        public static final String SEARCH = "search";
        public static final String PAGEABLE = "pageable";
    }

    private Pageable pageable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_search_results);

        Search search = getIntent().getParcelableExtra(Keys.SEARCH);
        Log.d(TAG, "Finding items for query: " + search.getQuery());
        new SearchTask(this).execute(search);
    }

}
