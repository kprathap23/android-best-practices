package com.nbempire.android.sample.service;

import com.nbempire.android.sample.domain.Item;

import java.util.List;

/**
 * Created by nbarrios on 24/09/14.
 */
public interface ItemService {

    public List<Item> findByTitulo(String titulo);
}
