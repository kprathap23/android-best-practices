package com.nbempire.android.sample.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by nbarrios on 24/09/14.
 * <p/>
 * Some parts of the code was taken from the official <a href="http://developer.android.com/reference/android/os/Parcelable.html">Android
 * documentation</a>
 */
public class Item implements Parcelable {

    private String id;
    private String title;
    private long price;
    private String subtitle;
    private String availableQuantity;
    private String thumbnail;
    private String initialQuantity;
    private String mainPictureUrl;
    private Date stopTime;

    public Item(String id, String title, long price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public Item(String id, Long price, Date stopTime) {
        this.id = id;
        this.price = price;
        this.stopTime = stopTime;
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

    public long getPrice() {
        return price;
    }

    public void setMainPictureUrl(String mainPictureUrl) {
        this.mainPictureUrl = mainPictureUrl;
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
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
        parcel.writeLong(price);
        parcel.writeString(subtitle);
        parcel.writeString(initialQuantity);
        parcel.writeString(availableQuantity);
        parcel.writeString(thumbnail);
        parcel.writeLong(stopTime != null ? stopTime.getTime() : null);
    }

    public Item(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.price = in.readLong();
        this.subtitle = in.readString();
        this.initialQuantity = in.readString();
        this.availableQuantity = in.readString();
        this.thumbnail = in.readString();
        this.stopTime = new Date(in.readLong());
    }

}
