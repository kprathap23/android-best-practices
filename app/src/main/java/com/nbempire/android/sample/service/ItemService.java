package com.nbempire.android.sample.service;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemService {

    public List<Item> find(Search search);
}
