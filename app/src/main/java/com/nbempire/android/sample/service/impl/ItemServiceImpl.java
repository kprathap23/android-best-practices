package com.nbempire.android.sample.service.impl;

import com.nbempire.android.sample.domain.Item;
import com.nbempire.android.sample.domain.Search;
import com.nbempire.android.sample.repository.ItemRepository;
import com.nbempire.android.sample.service.ItemService;
import com.nbempire.android.sample.util.Pageable;

/**
 * Created by nbarrios on 24/09/14.
 */
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Pageable<Item> find(Search search) {
        return itemRepository.findByTitle(search.getQuery(), search.getPaging());
    }

    @Override
    public Item findById(String id) {
        return itemRepository.findById(id);
    }
}
