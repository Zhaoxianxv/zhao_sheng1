package com.yfy.app.event.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Dep implements Parcelable {

    /**
     * departid : 1
     * departname : 学校文化
     */

    private String departid;
    private String departname;

    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departid);
        dest.writeString(this.departname);
    }

    public Dep() {
    }

    protected Dep(Parcel in) {
        this.departid = in.readString();
        this.departname = in.readString();
    }

    public static final Parcelable.Creator<Dep> CREATOR = new Parcelable.Creator<Dep>() {
        @Override
        public Dep createFromParcel(Parcel source) {
            return new Dep(source);
        }

        @Override
        public Dep[] newArray(int size) {
            return new Dep[size];
        }
    };
}
