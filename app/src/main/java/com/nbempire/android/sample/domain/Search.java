package com.nbempire.android.sample.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nbarrios on 24/09/14.
 */
public class Search implements Parcelable {

    private String query;
    private Paging paging;

    public Search() {
    }

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

    //  Everything from here is required to use Item as a Parcelable object.
    public static final Parcelable.Creator<Search> CREATOR = new Parcelable.Creator<Search>() {
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(query);
        parcel.writeParcelable(paging, 0);
    }

    public Search(Parcel in) {
        this.query = in.readString();
        this.paging = in.readParcelable(getClass().getClassLoader());
    }
}
