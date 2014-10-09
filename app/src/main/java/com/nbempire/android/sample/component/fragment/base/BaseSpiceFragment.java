package com.nbempire.android.sample.component.fragment.base;

import android.support.v4.app.Fragment;

import com.nbempire.android.sample.component.service.RetrofitSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * For fragments must use this base class because of fragments are a bit more difficult to handle.
 * You should take care of two additional things when using a SpiceManager within a Fragment :
 * <p/>
 * - check if SpiceManager.isStarted() before stopping it as the fragment can be stopped before
 * being started in some cases.
 * <p/>
 * - in your listeners, check if the Fragment.isAdded() before performing any manipulations on views
 * as the fragment views can be destroyed before onStop is called in a Fragment.
 * <p/>
 * Created by nbarrios on 09/10/14.
 *
 * @see <a href="https://github.com/stephanenicolas/robospice/wiki/Starter-Guide#notes-on-fragments-life-cycle">https://github.com/stephanenicolas/robospice/wiki/Starter-Guide#notes-on-fragments-life-cycle</a>
 */
public class BaseSpiceFragment extends Fragment {

    private SpiceManager spiceManager = new SpiceManager(RetrofitSpiceService.class);

    @Override
    public void onStart() {
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop() {
        // This check is only for classes that extends from Fragment as it says in: https://github.com/stephanenicolas/robospice/wiki/Starter-Guide#notes-on-fragments-life-cycle
        // Fragments are a bit more difficult to handle in Android. You should take care of two additional things when using a SpiceManager within a Fragment:
        // - check if SpiceManager.isStarted() before stopping it as the fragment can be stopped before being started in some cases.
        // - in your listeners, check if the Fragment.isAdded() before performing any manipulations on views as the fragment views can be destroyed before onStop is called in a Fragment.
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
