package com.nbempire.android.sample.util;

import com.nbempire.android.sample.domain.Paging;

import java.util.List;

/**
 * Created by nbarrios on 25/09/14.
 */
public interface Pageable<T> {

    public List<T> getResult();

    public Paging getPaging();

    public void update(Paging paging);

}
