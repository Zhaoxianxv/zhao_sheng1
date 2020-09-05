package com.yfy.app.property.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2018/3/1.
 */

public class BadObj implements Parcelable {
    private String num;
    private String content;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.num);
        dest.writeString(this.content);
    }

    public BadObj() {
    }

    public BadObj(String num, String content) {
        this.num = num;
        this.content = content;
    }

    protected BadObj(Parcel in) {
        this.num = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<BadObj> CREATOR = new Parcelable.Creator<BadObj>() {
        @Override
        public BadObj createFromParcel(Parcel source) {
            return new BadObj(source);
        }

        @Override
        public BadObj[] newArray(int size) {
            return new BadObj[size];
        }
    };
}
