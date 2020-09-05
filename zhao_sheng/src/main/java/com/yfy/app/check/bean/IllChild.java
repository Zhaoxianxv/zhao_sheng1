package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class IllChild implements Parcelable {

    /**
     * illtypename : 应到人数
     * illcount : 50
     */

    private String illtypename;
    private String illcount;
    private String statisticstype;
    private String statisticscount;
    private String statisticstypeid;

    public String getStatisticstypeid() {
        return statisticstypeid;
    }

    public void setStatisticstypeid(String statisticstypeid) {
        this.statisticstypeid = statisticstypeid;
    }

    public String getStatisticstype() {
        return statisticstype;
    }

    public void setStatisticstype(String statisticstype) {
        this.statisticstype = statisticstype;
    }

    public String getStatisticscount() {
        return statisticscount;
    }

    public void setStatisticscount(String statisticscount) {
        this.statisticscount = statisticscount;
    }

    public String getIlltypename() {
        return illtypename;
    }

    public void setIlltypename(String illtypename) {
        this.illtypename = illtypename;
    }

    public String getIllcount() {
        return illcount;
    }

    public void setIllcount(String illcount) {
        this.illcount = illcount;
    }

    public IllChild(String illtypename, String illcount) {
        this.illtypename = illtypename;
        this.illcount = illcount;
    }

    public IllChild() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.illtypename);
        dest.writeString(this.illcount);
        dest.writeString(this.statisticstype);
        dest.writeString(this.statisticscount);
        dest.writeString(this.statisticstypeid);
    }

    protected IllChild(Parcel in) {
        this.illtypename = in.readString();
        this.illcount = in.readString();
        this.statisticstype = in.readString();
        this.statisticscount = in.readString();
        this.statisticstypeid = in.readString();
    }

    public static final Creator<IllChild> CREATOR = new Creator<IllChild>() {
        @Override
        public IllChild createFromParcel(Parcel source) {
            return new IllChild(source);
        }

        @Override
        public IllChild[] newArray(int size) {
            return new IllChild[size];
        }
    };
}
