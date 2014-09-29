package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.task.ItemTask;

public class VIPActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "VIPActivity";

    public class Keys {
        public static final String ITEM = "item";
    }

    private ItemTask.ItemViewHolder viewHolder;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_vip);

        item = getIntent().getParcelableExtra(Keys.ITEM);

        viewHolder = new ItemTask.ItemViewHolder();
        viewHolder.title = (TextView) findViewById(R.id.item_title);
        viewHolder.subtitle = (TextView) findViewById(R.id.item_subtitle);
        viewHolder.price = (TextView) findViewById(R.id.item_price);
        viewHolder.initialQuantity = (TextView) findViewById(R.id.item_initial_quantity);
        viewHolder.availableQuantity = (TextView) findViewById(R.id.item_available_quantity);
        viewHolder.picture = (ImageView) findViewById(R.id.item_picture);

        if (savedInstanceState == null) {
            new ItemTask(this, viewHolder).execute(item.getId());
        }

        viewHolder.title.setText(item.getTitle());
        viewHolder.subtitle.setText(item.getSubtitle());
        viewHolder.price.setText(String.valueOf(item.getPrice()));

        if (item.getInitialQuantity() != null) {
            viewHolder.initialQuantity.append(item.getInitialQuantity());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState...");
        outState.putParcelable(Keys.ITEM, getIntent().getParcelableExtra(Keys.ITEM));

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState...");

        // This is here because the task will append the value to reuse the preffix and the value will be twice.
        viewHolder.availableQuantity.append(item.getAvailableQuantity());
    }
}
