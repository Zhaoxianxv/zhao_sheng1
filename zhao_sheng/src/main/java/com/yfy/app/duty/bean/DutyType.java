package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DutyType implements Parcelable {

    /**
     * typeid : 0
     * typename : 校级行政值周
     */

    private String typeid;
    private String typename;

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

    public DutyType() {
    }

    protected DutyType(Parcel in) {
        this.typeid = in.readString();
        this.typename = in.readString();
    }

    public static final Parcelable.Creator<DutyType> CREATOR = new Parcelable.Creator<DutyType>() {
        @Override
        public DutyType createFromParcel(Parcel source) {
            return new DutyType(source);
        }

        @Override
        public DutyType[] newArray(int size) {
            return new DutyType[size];
        }
    };
}
