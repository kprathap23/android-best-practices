package com.nbempire.android.sample.component.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.component.service.ItemTrackerService;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.nbempire.android.sample.task.ItemTask;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link ItemDetailFragment.OnFragmentInteractionListener} interface to handle interaction events.
 */
public class ItemDetailFragment extends Fragment {

    /**
     * Used for log messages.
     */
    private static final String TAG = "ItemDetailFragment";

    private OnFragmentInteractionListener mListener;

    private ItemTask.ItemViewHolder viewHolder;
    private Item item;
    private Activity context;

    public class Keys {
        public static final String ITEM = "item";
        private static final String PICTURE = "picture";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        item = context.getIntent().getParcelableExtra(Keys.ITEM);
        viewHolder = new ItemTask.ItemViewHolder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView... ");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        viewHolder.picture = (ImageView) view.findViewById(R.id.item_picture);
        Switch switchView = (Switch) view.findViewById(R.id.track_item_switch);

        if (savedInstanceState == null) {
            viewHolder.title = (TextView) view.findViewById(R.id.item_title);
            viewHolder.subtitle = (TextView) view.findViewById(R.id.item_subtitle);
            viewHolder.price = (TextView) view.findViewById(R.id.item_price);
            viewHolder.initialQuantity = (TextView) view.findViewById(R.id.item_initial_quantity);
            viewHolder.availableQuantity = (TextView) view.findViewById(R.id.item_available_quantity);

            new ItemTask(context, viewHolder).execute(item.getId());

            viewHolder.title.setText(item.getTitle());
            viewHolder.subtitle.setText(item.getSubtitle());
            viewHolder.price.setText(String.valueOf(item.getPrice()));

            viewHolder.initialQuantity.setText(String.valueOf(item.getInitialQuantity()));
            viewHolder.availableQuantity.setText(String.valueOf(item.getAvailableQuantity()));

            ItemService itemService = ItemServiceImpl.getInstance(context);
            if (itemService.isTracked(item.getId())) {
                switchView.setChecked(true);
            }
        } else {
            viewHolder.picture.setImageBitmap((Bitmap) savedInstanceState.getParcelable(Keys.PICTURE));
        }

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.v(TAG, "onCheckedChanged...");
                if (b) {
                    ItemTrackerService.startActionTrackItem(context, item.getId(), item.getTitle(), item.getPrice(), item.getStopTime());
                } else {
                    ItemTrackerService.startActionStopTrackingItem(context, item.getId());
                }

                if (mListener != null) {
                    mListener.onTrackItemSwitchSelected(compoundButton, b, item);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState...");
        outState.putParcelable(Keys.ITEM, context.getIntent().getParcelableExtra(Keys.ITEM));

        //  TODO : I should not be doing this. It's not the way to save an image.
        outState.putParcelable(Keys.PICTURE, ((BitmapDrawable) viewHolder.picture.getDrawable()).getBitmap());
        super.onSaveInstanceState(outState);
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     * <p/>
     * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onTrackItemSwitchSelected(CompoundButton compoundButton, boolean checked, Item item);
    }

}
