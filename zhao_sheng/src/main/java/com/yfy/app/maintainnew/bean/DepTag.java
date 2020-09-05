package com.yfy.app.maintainnew.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2017/5/11.
 */

public class DepTag implements Parcelable {

    /**
     * name : 电脑
     * id : 1
     */
    private String name;
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public DepTag() {
    }

    protected DepTag(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<DepTag> CREATOR = new Parcelable.Creator<DepTag>() {
        @Override
        public DepTag createFromParcel(Parcel source) {
            return new DepTag(source);
        }

        @Override
        public DepTag[] newArray(int size) {
            return new DepTag[size];
        }
    };
}
