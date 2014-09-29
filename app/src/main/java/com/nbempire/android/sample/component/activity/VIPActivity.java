package com.nbempire.android.sample.component.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.task.ItemTask;

public class VIPActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        Item item = getIntent().getParcelableExtra(Keys.ITEM);

        ItemTask.ItemViewHolder viewHolder = new ItemTask.ItemViewHolder();
        viewHolder.title = (TextView) findViewById(R.id.item_title);
        viewHolder.subtitle = (TextView) findViewById(R.id.item_subtitle);
        viewHolder.price = (TextView) findViewById(R.id.item_price);
        viewHolder.initialQuantity = (TextView) findViewById(R.id.item_initial_quantity);
        viewHolder.availableQuantity = (TextView) findViewById(R.id.item_available_quantity);

        viewHolder.title.setText(item.getTitle());
        viewHolder.subtitle.setText(item.getSubtitle());

        new ItemTask(viewHolder).execute(item.getId());
    }

    public class Keys {
        public static final String ITEM = "item";
    }
}
