package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class PlaneA implements Parcelable {
    private int type;//one child;two parent

    //childs
    private String dutyreporttype;
    private String typeid;
    private String checkcount;
    private String optioncount;
    private String issummarize;


    public PlaneA(int type) {
        this.type = type;
    }

    public PlaneA(int type, String dutyreporttype, String typeid, String checkcount,
                  String issummarize, String optioncount, String isedit) {
        this.type = type;
        this.dutyreporttype = dutyreporttype;
        this.typeid = typeid;
        this.checkcount = checkcount;
        this.issummarize = issummarize;
        this.optioncount = optioncount;
        this.isedit = isedit;
    }

    //parent
    private String date;
    private String ischeck;
    private String isedit;
    private String termname;
    private String weekname;

    public PlaneA(int type, String date, String ischeck,String isedit) {
        this.type = type;
        this.date = date;
        this.ischeck = ischeck;
        this.isedit = isedit;
    }


    public String getWeekname() {
        return weekname;
    }

    public void setWeekname(String weekname) {
        this.weekname = weekname;
    }

    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIsedit() {
        return isedit;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }


    public String getOptioncount() {
        return optioncount;
    }

    public void setOptioncount(String optioncount) {
        this.optioncount = optioncount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.dutyreporttype);
        dest.writeString(this.typeid);
        dest.writeString(this.checkcount);
        dest.writeString(this.optioncount);
        dest.writeString(this.issummarize);
        dest.writeString(this.date);
        dest.writeString(this.ischeck);
        dest.writeString(this.isedit);
    }

    protected PlaneA(Parcel in) {
        this.type = in.readInt();
        this.dutyreporttype = in.readString();
        this.typeid = in.readString();
        this.checkcount = in.readString();
        this.optioncount = in.readString();
        this.issummarize = in.readString();
        this.date = in.readString();
        this.ischeck = in.readString();
        this.isedit = in.readString();
    }

    public static final Parcelable.Creator<PlaneA> CREATOR = new Parcelable.Creator<PlaneA>() {
        @Override
        public PlaneA createFromParcel(Parcel source) {
            return new PlaneA(source);
        }

        @Override
        public PlaneA[] newArray(int size) {
            return new PlaneA[size];
        }
    };
}
