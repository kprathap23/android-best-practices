package com.nbempire.android.sample.domain;

/**
 * Created by nbarrios on 25/09/14.
 */
public class Paging {

    private Integer total;
    private int offset;
    private int limit;

    public Paging(int limit) {
        this.limit = limit;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
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
