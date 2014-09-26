package com.nbempire.android.sample.util.impl;

import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.util.Pageable;

import java.util.List;

/**
 * Created by nbarrios on 25/09/14.
 */
public class PageableImpl<T> implements Pageable<T> {

    private List<T> result;
    private Paging paging;

    public PageableImpl(List<T> result, Paging paging) {
        this.result = result;
        this.paging = paging;
    }

    @Override
    public List<T> getResult() {
        return this.result;
    }

    @Override
    public Paging getPaging() {
        return this.paging;
    }

}
