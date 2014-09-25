package com.nbempire.android.sample.domain;

/**
 * Created by nbarrios on 25/09/14.
 */
public class Paging {

    private int total;
    private int offset;
    private int limit;

    public Paging(int total, int limit) {
        this.total = total;
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
