package com.yfy.app;

import android.os.Parcel;
import android.os.Parcelable;

public class GType implements Parcelable {

    /**
     * typeid : 1
     * typename : 办公文具
     */

    private String typeid;
    private String typename;

    public GType(String typeid) {
        this.typeid = typeid;
    }

    public GType(String typeid, String typename) {
        this.typeid = typeid;
        this.typename = typename;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.typeid);
        dest.writeString(this.typename);
    }

    protected GType(Parcel in) {
        this.typeid = in.readString();
        this.typename = in.readString();
    }

    public static final Parcelable.Creator<GType> CREATOR = new Parcelable.Creator<GType>() {
        @Override
        public GType createFromParcel(Parcel source) {
            return new GType(source);
        }

        @Override
        public GType[] newArray(int size) {
            return new GType[size];
        }
    };
}
