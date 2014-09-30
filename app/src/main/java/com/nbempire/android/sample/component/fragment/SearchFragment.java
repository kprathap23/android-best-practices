package com.nbempire.android.sample.component.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.nbempire.android.sample.adapter.ItemAdapter;
import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.task.SearchTask;
import com.nbempire.android.sample.util.Pageable;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the Callbacks interface.
 */
public class SearchFragment extends ListFragment {

    /**
     * Used for log messages.
     */
    private static final String TAG = "SearchFragment";

    private ItemAdapter itemAdapter;

    private OnFragmentInteractionListener mListener;

    public class Keys {
        public static final String SEARCH = "search";
        public static final String PAGEABLE = "pageable";
        public static final String ITEM_ADAPTER = "itemAdapter";
    }

    public static SearchFragment newInstance() {
        Log.v(TAG, "newInstance...");
        return new SearchFragment();
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public SearchFragment() {
        Log.v(TAG, "SearchFragment...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");

        if (itemAdapter == null) {
            itemAdapter = new ItemAdapter(getActivity());
            setListAdapter(itemAdapter);
        }

        if (savedInstanceState == null) {
            Search search = getActivity().getIntent().getParcelableExtra(Keys.SEARCH);
            Log.d(TAG, "Finding items for query: " + search.getQuery());
            new SearchTask(getActivity(), (ItemAdapter) getListAdapter()).execute(search);
        } else {
            Bundle itemAdapterState = savedInstanceState.getBundle(Keys.ITEM_ADAPTER);
            ArrayList<Item> results = itemAdapterState.getParcelableArrayList(ItemAdapter.Keys.RESULTS);

            itemAdapter.addAll(results);
            itemAdapter.setLoadedPages(itemAdapterState.getIntArray(ItemAdapter.Keys.LOADED_PAGES_KEYS), itemAdapterState.getBooleanArray(ItemAdapter.Keys.LOADED_PAGES_VALUES));
            itemAdapter.setPageable((Pageable<Item>) itemAdapterState.getParcelable(ItemAdapter.Keys.PAGEABLE));

            Log.d(TAG, "Total items in adapter: " + itemAdapter.getCount());
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(TAG, "onAttach...");

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(TAG, "onDetach...");

        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.v(TAG, "onListItemClick...");

        if (mListener != null) {
            // Notify the active callbacks interface (the activity, if the fragment is attached to one) that an item has been selected.
            mListener.onSearchItemSelected(itemAdapter.getItem(position));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState...");
        outState.putBundle(Keys.ITEM_ADAPTER, itemAdapter.getState());

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
        void onSearchItemSelected(Item item);
    }

}
