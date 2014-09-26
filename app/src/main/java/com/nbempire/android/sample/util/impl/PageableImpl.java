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

    protected List<T> result;
    protected Paging paging;

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

    //  Everything from here is required to use Item as a Parcelable object.
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(result);
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
        this.result = new ArrayList<T>();
        in.readList(this.result, getClass().getClassLoader());

        this.paging = in.readParcelable(getClass().getClassLoader());
    }

}
