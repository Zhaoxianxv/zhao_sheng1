package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class IllAllBean implements Parcelable {

    /**
     * illid : 39
     * illdate : 2020/04/01
     * returndate :
     * illstate : 未返校
     * illtype : 传染病
     */

    private String illid;
    private String illdate;
    private String returndate;
    private String illstate;
    private String illtype;

    public String getIllid() {
        return illid;
    }

    public void setIllid(String illid) {
        this.illid = illid;
    }

    public String getIlldate() {
        return illdate;
    }

    public void setIlldate(String illdate) {
        this.illdate = illdate;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public String getIllstate() {
        return illstate;
    }

    public void setIllstate(String illstate) {
        this.illstate = illstate;
    }

    public String getIlltype() {
        return illtype;
    }

    public void setIlltype(String illtype) {
        this.illtype = illtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.illid);
        dest.writeString(this.illdate);
        dest.writeString(this.returndate);
        dest.writeString(this.illstate);
        dest.writeString(this.illtype);
    }

    public IllAllBean() {
    }

    protected IllAllBean(Parcel in) {
        this.illid = in.readString();
        this.illdate = in.readString();
        this.returndate = in.readString();
        this.illstate = in.readString();
        this.illtype = in.readString();
    }

    public static final Parcelable.Creator<IllAllBean> CREATOR = new Parcelable.Creator<IllAllBean>() {
        @Override
        public IllAllBean createFromParcel(Parcel source) {
            return new IllAllBean(source);
        }

        @Override
        public IllAllBean[] newArray(int size) {
            return new IllAllBean[size];
        }
    };
}
