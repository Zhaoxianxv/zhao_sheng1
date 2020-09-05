package com.yfy.app.duty.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Week implements Parcelable {

    /**
     * weekid : 1weekid
     * weekname : 第1周
     * startdate : 2019/02/18
     * enddate : 2019/02/24
     */

    private String weekid;
    private String weekname;
    private String startdate;
    private String enddate;
    private String weektitle;
    private String iscurrent;



    public String getIscurrent() {
        return iscurrent;
    }

    public void setIscurrent(String iscurrent) {
        this.iscurrent = iscurrent;
    }

    public String getWeektitle() {
        return weektitle;
    }

    public void setWeektitle(String weektitle) {
        this.weektitle = weektitle;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weekid);
        dest.writeString(this.weekname);
        dest.writeString(this.startdate);
        dest.writeString(this.enddate);
        dest.writeString(this.weektitle);
        dest.writeString(this.iscurrent);
    }

    public Week() {
    }

    protected Week(Parcel in) {
        this.weekid = in.readString();
        this.weekname = in.readString();
        this.startdate = in.readString();
        this.enddate = in.readString();
        this.weektitle = in.readString();
        this.iscurrent = in.readString();
    }

    public static final Parcelable.Creator<Week> CREATOR = new Parcelable.Creator<Week>() {
        @Override
        public Week createFromParcel(Parcel source) {
            return new Week(source);
        }

        @Override
        public Week[] newArray(int size) {
            return new Week[size];
        }
    };
}
