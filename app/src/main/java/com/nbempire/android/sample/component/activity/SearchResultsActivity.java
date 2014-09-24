package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.adapter.ItemAdapter;
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
        Item[] param = new Item[items.size()];
        ItemAdapter adapter = new ItemAdapter(this, items.toArray(param));

        resultsListView.setAdapter(adapter);
    }

}
