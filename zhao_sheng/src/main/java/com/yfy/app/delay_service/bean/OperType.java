package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OperType implements Parcelable {

    /**
     * opearid : 1
     * opearname : 正常出勤
     */

    private String opearid;
    private String opearname;
    private boolean iselect=false;

    public boolean isIselect() {
        return iselect;
    }

    public void setIselect(boolean iselect) {
        this.iselect = iselect;
    }

    public String getOpearid() {
        return opearid;
    }

    public void setOpearid(String opearid) {
        this.opearid = opearid;
    }

    public String getOpearname() {
        return opearname;
    }

    public void setOpearname(String opearname) {
        this.opearname = opearname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opearid);
        dest.writeString(this.opearname);
    }

    public OperType() {
    }

    protected OperType(Parcel in) {
        this.opearid = in.readString();
        this.opearname = in.readString();
    }

    public static final Creator<OperType> CREATOR = new Creator<OperType>() {
        @Override
        public OperType createFromParcel(Parcel source) {
            return new OperType(source);
        }

        @Override
        public OperType[] newArray(int size) {
            return new OperType[size];
        }
    };
}
