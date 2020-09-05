package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProLinkListBean implements Parcelable {

    /**
     * id : 1011.0
     * name : 电子章
     * image :
     * count : 0
     */

    private double id;
    private String name;
    private String image;
    private int count;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.count);
    }

    public ProLinkListBean() {
    }

    protected ProLinkListBean(Parcel in) {
        this.id = in.readDouble();
        this.name = in.readString();
        this.image = in.readString();
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<ProLinkListBean> CREATOR = new Parcelable.Creator<ProLinkListBean>() {
        @Override
        public ProLinkListBean createFromParcel(Parcel source) {
            return new ProLinkListBean(source);
        }

        @Override
        public ProLinkListBean[] newArray(int size) {
            return new ProLinkListBean[size];
        }
    };
}
