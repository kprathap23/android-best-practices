package com.nbempire.android.sample.util;

import android.os.Parcelable;

import com.nbempire.android.sample.domain.Paging;

import java.util.List;

/**
 * Created by nbarrios on 25/09/14.
 */
public interface Pageable<T> extends Parcelable {

    public String getQuery();

    void setQuery(String query);

    public List<T> getResults();

    public Paging getPaging();

}
