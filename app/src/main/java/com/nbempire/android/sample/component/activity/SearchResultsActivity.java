package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class SearchResultsActivity extends Activity {

    public class Keys {
        public static final String RESULTS = "results";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ListView resultsListView = (ListView) findViewById(R.id.searchResultsListView);

        List<Item> items = getIntent().getParcelableArrayListExtra(Keys.RESULTS);

        ArrayAdapter adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);

        resultsListView.setAdapter(adapter);
    }

}
