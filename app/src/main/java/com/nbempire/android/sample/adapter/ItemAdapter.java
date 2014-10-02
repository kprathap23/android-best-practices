package com.nbempire.android.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.nbempire.android.sample.manager.impl.ImageDownloadManagerImpl;
import com.nbempire.android.sample.task.SearchTask;
import com.nbempire.android.sample.util.Pageable;

import java.util.ArrayList;

/**
 * Created by nbarrios on 24/09/14.
 * <p/>
 * Some parts of the code was taken from the official <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">Android
 * documentation</a>
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemAdapter";

    private static final int DELTA_ITEMS_FOR_LOADING_NEW_ONES = 10;

    public class Keys {
        public static final String PAGEABLE = "pageable";
        public static final String LOADED_PAGES_KEYS = "keys";
        public static final String LOADED_PAGES_VALUES = "values";
        public static final String RESULTS = "results";
    }

    private Activity context;
    private Pageable<Item> pageable;
    private final LayoutInflater layoutInflater;

    private SparseBooleanArray loadedPages;

    public ItemAdapter(Activity context) {
        super(context, R.layout.item_in_list);

        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        loadedPages = new SparseBooleanArray();

        Log.d(TAG, "New instance of ItemAdapter created.");
    }

    /**
     * Read about holder pattern in <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">this
     * article of the Android documentation</a>.
     */
    static class ViewHolder {
        TextView title;
        TextView subtitle;
        TextView availableQuantity;
        ImageView thumbnail;
        TextView stopTime;
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
            viewHolder.stopTime = (TextView) convertView.findViewById(R.id.item_stop_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item eachItem = getItem(position);

        if (viewHolder.thumbnail != null) {
            ImageDownloadManagerImpl.getInstance().load(eachItem.getThumbnail(), viewHolder.thumbnail);
        }

        if (viewHolder.title != null) {
            viewHolder.title.setText(eachItem.getTitle() + " ($" + eachItem.getPrice() + ")");
        }

        if (viewHolder.stopTime != null) {
            String formatted = DateUtils.formatDateTime(context, eachItem.getStopTime().getTime(), DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR);
            viewHolder.stopTime.setText(context.getText(R.string.end_date) + " " + formatted);
        }

        if (viewHolder.subtitle != null) {
            viewHolder.subtitle.setText(eachItem.getSubtitle());
        }

        if (viewHolder.availableQuantity != null) {
            viewHolder.availableQuantity.setText(context.getText(R.string.still_left) + " " + eachItem.getAvailableQuantity());
        }

        return convertView;
    }

    private boolean loadNewPage(int currentPosition, int limit) {
        int fakePosition = currentPosition + DELTA_ITEMS_FOR_LOADING_NEW_ONES;
        return fakePosition % limit == 0 && !loadedPages.get(fakePosition, false);
    }

    private void loadResults(int offset) {
        Log.d(TAG, "Loading search results for offset: " + offset);
        Search search = new Search();
        search.setQuery(pageable.getQuery());
        search.setPaging(pageable.getPaging());
        search.getPaging().setOffset(offset);

        new SearchTask(context, this).execute(search);
    }

    /**
     * Get the current state of the ItemAdapter. It should be used to retrieve the current state and
     * save it for example before device orientation changes.
     *
     * @return A {@link android.os.Bundle} containing keys: {@link com.nbempire.android.sample.adapter.ItemAdapter.Keys#PAGEABLE},
     * {@link com.nbempire.android.sample.adapter.ItemAdapter.Keys#LOADED_PAGES_KEYS} and {@link
     * com.nbempire.android.sample.adapter.ItemAdapter.Keys#LOADED_PAGES_VALUES}
     */
    public Bundle getState() {
        Bundle state = new Bundle();
        state.putParcelable(Keys.PAGEABLE, pageable);
        state.putParcelableArrayList(Keys.RESULTS, getItems());

        int len = loadedPages.size();
        int[] keys = new int[len];
        boolean[] values = new boolean[len];

        for (int i = 0; i < len; i++) {
            keys[i] = loadedPages.keyAt(i);
            values[i] = loadedPages.valueAt(i);
        }
        state.putIntArray(Keys.LOADED_PAGES_KEYS, keys);
        state.putBooleanArray(Keys.LOADED_PAGES_VALUES, values);

        return state;
    }

    private ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<Item>();

        int len = getCount();
        for (int i = 0; i < len; i++) {
            items.add(getItem(i));
        }

        return items;
    }

    public void setPageable(Pageable<Item> pageable) {
        this.pageable = pageable;
    }

    public void setLoadedPages(int[] pagesKey, boolean[] loadedPagesValues) {
        int len = pagesKey.length;

        for (int i = 0; i < len; i++) {
            loadedPages.put(pagesKey[i], loadedPagesValues[i]);
        }
    }
}
