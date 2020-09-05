package com.yfy.app.seal.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Opear implements Parcelable {

    /**
     * opearid : 100
     * opeartitle : 拒绝申请
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

    public Opear() {
    }

    protected Opear(Parcel in) {
        this.opearid = in.readString();
        this.opeartitle = in.readString();
    }

    public static final Parcelable.Creator<Opear> CREATOR = new Parcelable.Creator<Opear>() {
        @Override
        public Opear createFromParcel(Parcel source) {
            return new Opear(source);
        }

        @Override
        public Opear[] newArray(int size) {
            return new Opear[size];
        }
    };
}
