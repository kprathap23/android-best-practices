package com.nbempire.android.sample.repository.impl;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemRepositoryImpl implements ItemRepository {
    @Override
    public List<Item> findByTitulo(String titulo) {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("ipod nano"));
        items.add(new Item("ipod touch"));
        items.add(new Item("ipod touch 16gb"));
        items.add(new Item("ipod nano 32gb"));

        return items;
    }
}
