package com.example.pyrov.mywallpapers.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HitsItem implements Parcelable {
    private String previewURL;
    private String fullHDURL;
    private String imageURL;
    private String largeImageURL;

    public String getPreviewURL() {
        return previewURL;
    }

    public String getFullHDURL() {
        return fullHDURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.previewURL);
        dest.writeString(this.fullHDURL);
        dest.writeString(this.imageURL);
        dest.writeString(this.largeImageURL);
    }

    public HitsItem() {
    }

    protected HitsItem(Parcel in) {
        this.previewURL = in.readString();
        this.fullHDURL = in.readString();
        this.imageURL = in.readString();
        this.largeImageURL = in.readString();
    }

    public static final Parcelable.Creator<HitsItem> CREATOR = new Parcelable.Creator<HitsItem>() {
        @Override
        public HitsItem createFromParcel(Parcel source) {
            return new HitsItem(source);
        }

        @Override
        public HitsItem[] newArray(int size) {
            return new HitsItem[size];
        }
    };
}
