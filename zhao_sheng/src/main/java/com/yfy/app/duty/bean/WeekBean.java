package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

public class WeekBean implements Parcelable {
    //child  parent type
    private int type;

    public WeekBean() {
        this.type=TagFinal.ONE_INT;
    }

    //child
    private String weekid;
    private String weekname;
    private String startdate;
    private String enddate;

    public WeekBean(String weekid, String weekname, String startdate, String enddate) {
        this.weekid = weekid;
        this.weekname = weekname;
        this.startdate = startdate;
        this.enddate = enddate;
        this.type = TagFinal.TWO_INT;
    }

    //parent
    private String termid;
    private String termname;
    private String isCurrentTerm;

    public WeekBean(String startdate, String enddate, String termid, String termname, String isCurrentTerm) {
        this.startdate = startdate;
        this.enddate = enddate;
        this.termid = termid;
        this.termname = termname;
        this.isCurrentTerm = isCurrentTerm;
        this.type = TagFinal.THREE_INT;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWeekid() {
        return weekid;
    }

    public void setWeekid(String weekid) {
        this.weekid = weekid;
    }

    public String getWeekname() {
        return weekname;
    }

    public void setWeekname(String weekname) {
        this.weekname = weekname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getTermid() {
        return termid;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public String getIsCurrentTerm() {
        return isCurrentTerm;
    }

    public void setIsCurrentTerm(String isCurrentTerm) {
        this.isCurrentTerm = isCurrentTerm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.weekid);
        dest.writeString(this.weekname);
        dest.writeString(this.startdate);
        dest.writeString(this.enddate);
        dest.writeString(this.termid);
        dest.writeString(this.termname);
        dest.writeString(this.isCurrentTerm);
    }

    protected WeekBean(Parcel in) {
        this.type = in.readInt();
        this.weekid = in.readString();
        this.weekname = in.readString();
        this.startdate = in.readString();
        this.enddate = in.readString();
        this.termid = in.readString();
        this.termname = in.readString();
        this.isCurrentTerm = in.readString();
    }

    public static final Parcelable.Creator<WeekBean> CREATOR = new Parcelable.Creator<WeekBean>() {
        @Override
        public WeekBean createFromParcel(Parcel source) {
            return new WeekBean(source);
        }

        @Override
        public WeekBean[] newArray(int size) {
            return new WeekBean[size];
        }
    };
}
