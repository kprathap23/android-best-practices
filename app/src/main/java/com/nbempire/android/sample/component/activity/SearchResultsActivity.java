package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.adapter.ItemAdapter;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.util.Pageable;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchResultsActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchResultsActivity";

    public class Keys {
        public static final String PAGE = "page";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_search_results);

        ListView resultsListView = (ListView) findViewById(R.id.searchResultsListView);

        Pageable<Item> pageable = getIntent().getParcelableExtra(Keys.PAGE);

        List<Item> items = pageable.getResult();
        Item[] param = new Item[items.size()];

        //  TODO : Check whether this new instance of ItemAdapter is necessary or not.
        resultsListView.setAdapter(new ItemAdapter(this, items.toArray(param)));
    }

}
