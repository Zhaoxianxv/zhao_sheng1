package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class IllGroup implements Parcelable {

    /**
     * groupid : 51
     * groupname : 五年级一班
     */

    private String groupid;
    private String groupname;
    private String statisticsid;
    private List<IllChild> illstatistics;
    private List<MasterUser> groupmaster;
    private ArrayList<CheckStu> illuserlist;
    private List<CheckState> states;
    private String statisticsname;


    public String getStatisticsid() {
        return statisticsid;
    }

    public void setStatisticsid(String statisticsid) {
        this.statisticsid = statisticsid;
    }

    private List<IllChild> statisticslist;


    public String getStatisticsname() {
        return statisticsname;
    }

    public void setStatisticsname(String statisticsname) {
        this.statisticsname = statisticsname;
    }

    public List<IllChild> getStatisticslist() {
        return statisticslist;
    }

    public void setStatisticslist(List<IllChild> statisticslist) {
        this.statisticslist = statisticslist;
    }

    public ArrayList<CheckStu> getIlluserlist() {
        return illuserlist;
    }

    public void setIlluserlist(ArrayList<CheckStu> illuserlist) {
        this.illuserlist = illuserlist;
    }

    public List<MasterUser> getGroupmaster() {
        return groupmaster;
    }

    public void setGroupmaster(List<MasterUser> groupmaster) {
        this.groupmaster = groupmaster;
    }



    public List<CheckState> getStates() {
        return states;
    }

    public void setStates(List<CheckState> states) {
        this.states = states;
    }

    public List<IllChild> getIllstatistics() {
        return illstatistics;
    }

    public void setIllstatistics(List<IllChild> illstatistics) {
        this.illstatistics = illstatistics;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public IllGroup() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupid);
        dest.writeString(this.groupname);
        dest.writeString(this.statisticsid);
        dest.writeTypedList(this.illstatistics);
        dest.writeTypedList(this.groupmaster);
        dest.writeTypedList(this.illuserlist);
        dest.writeTypedList(this.states);
        dest.writeString(this.statisticsname);
        dest.writeTypedList(this.statisticslist);
    }

    protected IllGroup(Parcel in) {
        this.groupid = in.readString();
        this.groupname = in.readString();
        this.statisticsid = in.readString();
        this.illstatistics = in.createTypedArrayList(IllChild.CREATOR);
        this.groupmaster = in.createTypedArrayList(MasterUser.CREATOR);
        this.illuserlist = in.createTypedArrayList(CheckStu.CREATOR);
        this.states = in.createTypedArrayList(CheckState.CREATOR);
        this.statisticsname = in.readString();
        this.statisticslist = in.createTypedArrayList(IllChild.CREATOR);
    }

    public static final Creator<IllGroup> CREATOR = new Creator<IllGroup>() {
        @Override
        public IllGroup createFromParcel(Parcel source) {
            return new IllGroup(source);
        }

        @Override
        public IllGroup[] newArray(int size) {
            return new IllGroup[size];
        }
    };
}
