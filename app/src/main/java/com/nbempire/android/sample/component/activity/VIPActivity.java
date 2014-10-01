package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.component.service.ItemTrackerService;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.nbempire.android.sample.task.ItemTask;

public class VIPActivity extends Activity {

    /**
     * Used for log messages.
     */
    private static final String TAG = "VIPActivity";
    private ItemService itemService;

    public class Keys {
        public static final String ITEM = "item";
    }

    private ItemTask.ItemViewHolder viewHolder;
    private Item item;
    private final Activity context;

    public VIPActivity() {
        this.context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_vip);

        itemService = new ItemServiceImpl(this, new ItemRepositoryImpl(this));
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

        Switch switchView = (Switch) findViewById(R.id.track_item_switch);

        if (itemService.isTracked(item.getId())) {
            switchView.setChecked(true);
        }

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.v(TAG, "onCheckedChanged...");
                if (b) {
                    ItemTrackerService.startActionTrackItem(context, item.getId(), item.getPrice(), item.getStopTime());
                } else {
                    ItemTrackerService.startActionStopTrackingItem(context, item.getId());
                }
            }
        });
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
