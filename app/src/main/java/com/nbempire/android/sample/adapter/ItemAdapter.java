package com.nbempire.android.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.manager.ImageDownloadManager;
import com.nbempire.android.sample.manager.impl.ImageDownloadManagerImpl;
import com.nbempire.android.sample.task.SearchTask;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 * <p/>
 * Some parts of the code was taken from the official <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">Android documentation</a>
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemAdapter";

    private static final int DELTA_ITEMS_FOR_LOADING_NEW_ONES = 10;

    private Activity context;
    private Pageable<Item> pageable;
    private final LayoutInflater layoutInflater;
    private final ImageDownloadManager imageDownloadManager;

    private SparseBooleanArray loadedPages;

    public ItemAdapter(Activity context, Pageable<Item> pageable) {
        super(context, R.layout.item_in_list, pageable.getResult());

        this.context = context;
        this.pageable = pageable;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageDownloadManager = ImageDownloadManagerImpl.getInstance();

        loadedPages = new SparseBooleanArray();

        Log.d(TAG, "New instance of ItemAdapter created.");
    }

    /**
     * Read about holder pattern in <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">this article of the Android documentation</a>.
     */
    static class ViewHolder {
        TextView title;
        TextView subtitle;
        TextView availableQuantity;
        ImageView thumbnail;
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        int positionInApi = position + DELTA_ITEMS_FOR_LOADING_NEW_ONES;
        if (loadNewPage(position, pageable.getPaging().getLimit())) {
            loadedPages.put(positionInApi, true);
            loadResults(positionInApi);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_in_list, parent, false);

            viewHolder.title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.subtitle = (TextView) convertView.findViewById(R.id.item_subtitle);
            viewHolder.availableQuantity = (TextView) convertView.findViewById(R.id.item_quantity);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.item_thumbnail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item cadaItem = getItem(position);

        if (viewHolder.thumbnail != null) {
            imageDownloadManager.load(cadaItem.getThumbnail(), viewHolder.thumbnail);
        }

        if (viewHolder.title != null) {
            viewHolder.title.setText(cadaItem.getTitle());
        }

        if (viewHolder.subtitle != null) {
            viewHolder.subtitle.setText(cadaItem.getSubtitle());
        }

        if (viewHolder.availableQuantity != null) {
            viewHolder.availableQuantity.setText(cadaItem.getAvailableQuantity());
        }

        return convertView;
    }

    private boolean loadNewPage(int currentPosition, int limit) {
        int fakePosition = currentPosition + DELTA_ITEMS_FOR_LOADING_NEW_ONES;
        return fakePosition % limit == 0 && !loadedPages.get(fakePosition, false);
    }

    private void loadResults(int offset) {
        Search search = new Search();
        search.setQuery(pageable.getQuery());
        search.setPaging(pageable.getPaging());
        search.getPaging().setOffset(offset);

        new SearchTask(context).execute(search);
    }
}
