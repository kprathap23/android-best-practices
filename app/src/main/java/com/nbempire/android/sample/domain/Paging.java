package com.nbempire.android.sample.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nbarrios on 25/09/14.
 */
public class Paging implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(total);
        parcel.writeInt(offset);
        parcel.writeInt(limit);
    }

    //  From here everything is required to use Item as a Parcelable object.
    public static final Parcelable.Creator<Paging> CREATOR = new Parcelable.Creator<Paging>() {
        public Paging createFromParcel(Parcel in) {
            return new Paging(in);
        }

        public Paging[] newArray(int size) {
            return new Paging[size];
        }
    };

    public Paging(Parcel in) {
        this.total = in.readInt();
        this.offset = in.readInt();
        this.limit = in.readInt();
    }
}
