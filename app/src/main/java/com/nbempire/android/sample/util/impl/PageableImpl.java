package com.nbempire.android.sample.util.impl;

import android.os.Parcel;
import android.os.Parcelable;

import com.nbempire.android.sample.domain.Paging;
import com.nbempire.android.sample.util.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbarrios on 25/09/14.
 */
public class PageableImpl<T extends Parcelable> implements Pageable<T> {

    private String query;
    private List<T> results;
    private Paging paging;

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public List<T> getResults() {
        return this.results;
    }

    @Override
    public Paging getPaging() {
        return this.paging;
    }


    //  Everything from here is required to use Item as a Parcelable object.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(query);
        parcel.writeList(results);
        parcel.writeParcelable(paging, 0);
    }

    public static final Parcelable.Creator<PageableImpl> CREATOR = new Parcelable.Creator<PageableImpl>() {
        public PageableImpl createFromParcel(Parcel in) {
            return new PageableImpl(in);
        }

        public PageableImpl[] newArray(int size) {
            return new PageableImpl[size];
        }
    };

    public PageableImpl(Parcel in) {
        this.query = in.readString();

        this.results = new ArrayList<T>();
        in.readList(this.results, getClass().getClassLoader());

        this.paging = in.readParcelable(getClass().getClassLoader());
    }

}
