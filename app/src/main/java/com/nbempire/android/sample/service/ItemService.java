package com.nbempire.android.sample.service;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemService {

    public Pageable<Item> find(Search search);
}
