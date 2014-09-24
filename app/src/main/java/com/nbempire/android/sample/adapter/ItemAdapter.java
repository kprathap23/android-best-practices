package com.nbempire.android.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nbempire.android.sample.domain.Item;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private final LayoutInflater layoutInflater;

    public ItemAdapter(Context context, Item[] items) {
        super(context, android.R.layout.simple_list_item_1, items);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        Item anItem = getItem(position);
        ((TextView) view.findViewById(android.R.id.text1)).setText(anItem.getTitulo());

        return view;
    }
}
