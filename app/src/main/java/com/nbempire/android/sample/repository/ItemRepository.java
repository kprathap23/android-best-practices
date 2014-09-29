package com.nbempire.android.sample.repository;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemRepository {

    Pageable<Item> findByTitle(String title, Paging paging);

    Item findById(String id);
}
