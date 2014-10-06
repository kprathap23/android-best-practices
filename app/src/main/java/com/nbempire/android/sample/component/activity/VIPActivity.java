package com.nbempire.android.sample.component.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.CompoundButton;

import com.nbempire.android.sample.R;
import com.nbempire.android.sample.component.fragment.ItemDetailFragment;
import com.nbempire.android.sample.domain.Item;

public class VIPActivity extends FragmentActivity implements ItemDetailFragment.OnFragmentInteractionListener {

    /**
     * Used for log messages.
     */
    private static final String TAG = "VIPActivity";

    public static class Keys {
        public static final String ITEM = ItemDetailFragment.Keys.ITEM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate...");
        setContentView(R.layout.activity_vip);
    }

    @Override
    public void onTrackItemSwitchSelected(CompoundButton compoundButton, boolean checked, Item item) {
        Log.v(TAG, "onTrackItemSwitchSelected...");
    }
}
