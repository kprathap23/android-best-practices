package com.nbempire.android.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private final LayoutInflater layoutInflater;

    public ItemAdapter(Context context, Item[] items) {
        super(context, R.layout.item_in_list, items);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_in_list, parent, false);
        } else {
            view = convertView;
        }

        Item cadaItem = getItem(position);

        TextView titulo = (TextView) view.findViewById(R.id.item_title);
        if (titulo != null) {
            titulo.setText(cadaItem.getTitulo());
        }

        TextView subtitulo = (TextView) view.findViewById(R.id.item_subtitle);
        if (subtitulo != null) {
            subtitulo.setText(cadaItem.getSubtitulo());
        }

        TextView cantidadDisponible = (TextView) view.findViewById(R.id.item_quantity);
        if (cantidadDisponible != null) {
            cantidadDisponible.setText(cadaItem.getCantidadDisponible());
        }

        return view;
    }
}
