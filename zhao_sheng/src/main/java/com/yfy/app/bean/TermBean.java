package com.yfy.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TermBean implements Parcelable {

    /**
     * isnow : 0
     * name : 2015-2016上期
     * id : 1
     */

    private String isnow;
    private String name;
    private String datemax;
    private String datemin;
    private String id;

    public String getIsnow() {
        return isnow;
    }

    public void setIsnow(String isnow) {
        this.isnow = isnow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.isnow);
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public TermBean() {
    }

    protected TermBean(Parcel in) {
        this.isnow = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Creator<TermBean> CREATOR = new Creator<TermBean>() {
        @Override
        public TermBean createFromParcel(Parcel source) {
            return new TermBean(source);
        }

        @Override
        public TermBean[] newArray(int size) {
            return new TermBean[size];
        }
    };
}
