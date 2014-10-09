package com.nbempire.android.sample.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.nbempire.android.sample.Application;
import com.nbempire.android.sample.R;
import com.nbempire.android.sample.component.fragment.ItemDetailFragment;
import com.nbempire.android.sample.component.fragment.SearchFragment;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.task.SearchTask;


public class SearchActivity extends FragmentActivity implements ItemDetailFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchActivity";
    private static final int ITEMS_PER_PAGE = 15;

    private Search search;
    private EditText query;

    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");

        setContentView(R.layout.activity_search);

        query = (EditText) findViewById(R.id.searchQuery);
        query.setText(getSharedPreferences(Application.Keys.APP_SHARED_PREFERENCES, MODE_PRIVATE).getString(SearchTask.LAST_QUERY, null));

        search = new Search();

        supportFragmentManager = getSupportFragmentManager();
    }

    public void findItems(View view) {
        Log.v(TAG, "findItems...");

        search.setQuery(query.getText().toString());
        search.setPaging(new Paging(ITEMS_PER_PAGE));

        Log.d(TAG, "Loading search results...");

        FrameLayout container = (FrameLayout) findViewById(R.id.search_fragment_container);
        if (container == null) {
            Intent resultsIntent = new Intent(this, SearchResultsActivity.class);
            resultsIntent.putExtra(SearchResultsActivity.Keys.SEARCH, search);
            startActivity(resultsIntent);
        } else {
            getIntent().putExtra(SearchFragment.Keys.SEARCH, search);
            SearchFragment searchFragment = new SearchFragment();
            searchFragment.setArguments(getIntent().getExtras());
            supportFragmentManager.beginTransaction().add(R.id.search_fragment_container, searchFragment).commit();
        }
    }

    @Override
    public void onSearchItemSelected(Item item) {
        Log.v(TAG, "onSearchItemSelected...");
        Log.i(TAG, "Opening VIP for item: " + item.getTitle());

        // Create a new Fragment to be placed in the activity layout
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();

        getIntent().putExtra(ItemDetailFragment.Keys.ITEM, item);
        itemDetailFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'item_detail_container' FrameLayout
        supportFragmentManager.beginTransaction().add(R.id.item_detail_container, itemDetailFragment).commit();
    }

    @Override
    public void onTrackItemSwitchSelected(CompoundButton compoundButton, boolean checked, Item item) {
    }
}
