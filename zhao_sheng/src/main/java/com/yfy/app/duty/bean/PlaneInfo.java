package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PlaneInfo implements Parcelable {

    /**
     * date : 2019/03/06
     * ischeck : true
     */

    private String date;
    private String ischeck;
    private String isedit;
    private String termname;
    private String weekname;


    private boolean is_Select=false;
    private List<PlaneBean> dutyreport_detail;


    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public String getWeekname() {
        return weekname;
    }

    public void setWeekname(String weekname) {
        this.weekname = weekname;
    }

    public boolean isIs_Select() {
        return is_Select;
    }

    public void setIs_Select(boolean is_Select) {
        this.is_Select = is_Select;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getIsedit() {
        return isedit;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

    public List<PlaneBean> getDutyreport_detail() {
        return dutyreport_detail;
    }

    public void setDutyreport_detail(List<PlaneBean> dutyreport_detail) {
        this.dutyreport_detail = dutyreport_detail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.ischeck);
        dest.writeString(this.isedit);
        dest.writeByte(this.is_Select ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.dutyreport_detail);
    }

    public PlaneInfo() {
    }

    protected PlaneInfo(Parcel in) {
        this.date = in.readString();
        this.ischeck = in.readString();
        this.isedit = in.readString();
        this.is_Select = in.readByte() != 0;
        this.dutyreport_detail = in.createTypedArrayList(PlaneBean.CREATOR);
    }

    public static final Parcelable.Creator<PlaneInfo> CREATOR = new Parcelable.Creator<PlaneInfo>() {
        @Override
        public PlaneInfo createFromParcel(Parcel source) {
            return new PlaneInfo(source);
        }

        @Override
        public PlaneInfo[] newArray(int size) {
            return new PlaneInfo[size];
        }
    };
}
