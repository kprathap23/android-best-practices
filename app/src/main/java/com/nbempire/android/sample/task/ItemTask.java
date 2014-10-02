package com.nbempire.android.sample.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbempire.android.sample.component.fragment.ItemDetailFragment;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.manager.impl.ImageDownloadManagerTPE;
import com.nbempire.android.sample.repository.impl.ItemRepositoryImpl;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;

/**
 * Created by nbarrios on 29/09/14.
 */
public class ItemTask extends AsyncTask<String, Integer, Item> {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemTask";

    private final Activity context;
    private final ItemViewHolder viewHolder;
    private final ItemService itemService;

    public static class ItemViewHolder {
        public TextView title;
        public TextView subtitle;
        public TextView price;
        public TextView initialQuantity;
        public TextView availableQuantity;
        public ImageView picture;
    }

    public ItemTask(Activity context, ItemViewHolder viewHolder) {
        this.context = context;
        this.viewHolder = viewHolder;
        this.itemService = new ItemServiceImpl(context, new ItemRepositoryImpl(context));
    }

    @Override
    protected Item doInBackground(String... strings) {
        Log.v(TAG, "doInBackground...");

        return itemService.findById(strings[0]);
    }

    @Override
    protected void onPostExecute(Item item) {
//        ImageDownloadManagerImpl.getInstance().load(item.getMainPictureUrl(), viewHolder.picture);
        ImageDownloadManagerTPE.load(item.getMainPictureUrl(), viewHolder.picture);

        viewHolder.title.setText(item.getTitle());
        viewHolder.subtitle.setText(item.getSubtitle());
        viewHolder.price.setText(String.valueOf(item.getPrice()));
        viewHolder.initialQuantity.append(item.getInitialQuantity());
        viewHolder.availableQuantity.append(item.getAvailableQuantity());

        context.getIntent().putExtra(ItemDetailFragment.Keys.ITEM, item);
    }
}
