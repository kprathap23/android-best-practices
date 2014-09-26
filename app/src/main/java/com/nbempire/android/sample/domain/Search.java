package com.nbempire.android.sample.domain;

/**
 * Created by nbarrios on 24/09/14.
 */
public class Search {

    private String query;
    private Paging paging;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
