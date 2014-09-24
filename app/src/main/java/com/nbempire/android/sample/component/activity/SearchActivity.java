package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;

import java.util.List;


public class SearchActivity extends Activity {

    private static final String TAG = "SearchActivity";
    private ItemServiceImpl itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.itemService = new ItemServiceImpl(new ItemRepositoryImpl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void findItems(View view) {
        Log.v(TAG, "findItems...");

        EditText query = (EditText) findViewById(R.id.searchQuery);

        Log.d(TAG, "Finding items for query: " + query.getText().toString());
        List<Item> items = itemService.findByTitulo(query.getText().toString());

        //  Print results...
        Log.d(TAG, "Found items: " + items.size());
        for (Item item : items) {
            Log.d(TAG, "Item title: " + item.getTitulo());
        }


    }
}
