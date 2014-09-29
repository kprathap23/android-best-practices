package com.nbempire.android.sample.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nbarrios on 24/09/14.
 * <p/>
 * Some parts of the code was taken from the official <a href="http://developer.android.com/reference/android/os/Parcelable.html">Android
 * documentation</a>
 */
public class Item implements Parcelable {

    private String id;
    private String title;
    private String subtitle;
    private String availableQuantity;
    private String thumbnail;
    private String initialQuantity;

    public Item(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setInitialQuantity(String initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public String getInitialQuantity() {
        return initialQuantity;
    }

    //  Everything from here is required to use Item as a Parcelable object.
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeString(initialQuantity);
        parcel.writeString(availableQuantity);
        parcel.writeString(thumbnail);
    }

    public Item(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.initialQuantity = in.readString();
        this.availableQuantity = in.readString();
        this.thumbnail = in.readString();
    }

}
