package com.nbempire.android.sample.repository;

import com.nbempire.android.sample.domain.Item;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemRepository {

    public List<Item> findByTitle(String title);
}
