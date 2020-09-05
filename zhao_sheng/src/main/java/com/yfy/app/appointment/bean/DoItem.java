package com.yfy.app.appointment.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DoItem implements Parcelable {

    /**
     * opearid : 2
     * opeartitle : 拒绝
     */

    private String opearid;
    private String opeartitle;

    public String getOpearid() {
        return opearid;
    }

    public void setOpearid(String opearid) {
        this.opearid = opearid;
    }

    public String getOpeartitle() {
        return opeartitle;
    }

    public void setOpeartitle(String opeartitle) {
        this.opeartitle = opeartitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opearid);
        dest.writeString(this.opeartitle);
    }

    public DoItem() {
    }

    protected DoItem(Parcel in) {
        this.opearid = in.readString();
        this.opeartitle = in.readString();
    }

    public static final Parcelable.Creator<DoItem> CREATOR = new Parcelable.Creator<DoItem>() {
        @Override
        public DoItem createFromParcel(Parcel source) {
            return new DoItem(source);
        }

        @Override
        public DoItem[] newArray(int size) {
            return new DoItem[size];
        }
    };
}
