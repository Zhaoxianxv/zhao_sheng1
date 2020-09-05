package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaneBean implements Parcelable {


    /**
     * dutyreporttype : 校级行政值周
     * typeid : 0
     * checkcount : 3
     * issummarize : false
     */

    private String dutyreporttype;
    private String typeid;
    private String checkcount;
    private String issummarize;
    private String optioncount;

    public String getOptioncount() {
        return optioncount;
    }

    public void setOptioncount(String optioncount) {
        this.optioncount = optioncount;
    }

    public String getDutyreporttype() {
        return dutyreporttype;
    }

    public void setDutyreporttype(String dutyreporttype) {
        this.dutyreporttype = dutyreporttype;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(String checkcount) {
        this.checkcount = checkcount;
    }

    public String getIssummarize() {
        return issummarize;
    }

    public void setIssummarize(String issummarize) {
        this.issummarize = issummarize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dutyreporttype);
        dest.writeString(this.typeid);
        dest.writeString(this.checkcount);
        dest.writeString(this.issummarize);
    }

    public PlaneBean() {
    }

    protected PlaneBean(Parcel in) {
        this.dutyreporttype = in.readString();
        this.typeid = in.readString();
        this.checkcount = in.readString();
        this.issummarize = in.readString();
    }

    public static final Parcelable.Creator<PlaneBean> CREATOR = new Parcelable.Creator<PlaneBean>() {
        @Override
        public PlaneBean createFromParcel(Parcel source) {
            return new PlaneBean(source);
        }

        @Override
        public PlaneBean[] newArray(int size) {
            return new PlaneBean[size];
        }
    };
}
