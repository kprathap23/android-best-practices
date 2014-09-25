package com.nbempire.android.sample.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.manager.ImageDownloadManager;
import com.nbempire.android.sample.manager.impl.ImageDownloadManagerImpl;

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

    private final LayoutInflater layoutInflater;
    private final ImageDownloadManager imageDownloadManager;

    public ItemAdapter(Context context, Item[] items) {
        super(context, R.layout.item_in_list, items);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageDownloadManager = ImageDownloadManagerImpl.getInstance();

        Log.d(TAG, "New instance of ItemAdapter created.");
    }

    /**
     * Populate new items in the list.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_in_list, parent, false);

            viewHolder.titulo = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.subtitulo = (TextView) convertView.findViewById(R.id.item_subtitle);
            viewHolder.cantidadDisponible = (TextView) convertView.findViewById(R.id.item_quantity);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.item_thumbnail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item cadaItem = getItem(position);

        if (viewHolder.thumbnail != null) {
            imageDownloadManager.load(cadaItem.getThumbnail(), viewHolder.thumbnail);
        }

        if (viewHolder.titulo != null) {
            viewHolder.titulo.setText(cadaItem.getTitulo());
        }

        if (viewHolder.subtitulo != null) {
            viewHolder.subtitulo.setText(cadaItem.getSubtitulo());
        }

        if (viewHolder.cantidadDisponible != null) {
            viewHolder.cantidadDisponible.setText(cadaItem.getCantidadDisponible());
        }

        return convertView;
    }

    /**
     * Read about holder pattern in <a href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html">this article of the Android documentation</a>.
     */
    static class ViewHolder {
        TextView titulo;
        TextView subtitulo;
        TextView cantidadDisponible;
        ImageView thumbnail;
    }
}
