package com.nbempire.android.sample.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nbarrios on 24/09/14.
 */
public class Item implements Parcelable {

    private String titulo;
    private String subtitulo;
    private String cantidadDisponible;

    public Item(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setCantidadDisponible(String cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getCantidadDisponible() {
        return cantidadDisponible;
    }

    //    Override it because of ArrayAdapter implementation... it sucks!
    @Override
    public String toString() {
        return titulo;
    }

    //  From here everything is required to use Item as a Parcelable object.
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
        parcel.writeString(titulo);
        parcel.writeString(subtitulo);
        parcel.writeString(cantidadDisponible);
    }

    public Item(Parcel in) {
        this.titulo = in.readString();
        this.subtitulo = in.readString();
        this.cantidadDisponible = in.readString();
    }

}
