package com.nbempire.android.sample.repository.request;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.repository.ItemRemoteRepository;
import com.nbempire.android.sample.service.impl.ItemServiceImpl;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by nbarrios on 09/10/14.
 */
public class ItemSpiceRequest extends RetrofitSpiceRequest<Item, ItemRemoteRepository> {

    private String id;

    public ItemSpiceRequest(String id) {
        super(Item.class, ItemRemoteRepository.class);
        this.id = id;
    }

    @Override
    public Item loadDataFromNetwork() throws Exception {
        return ItemServiceImpl.parse(getService().findById(this.id));
    }

}
