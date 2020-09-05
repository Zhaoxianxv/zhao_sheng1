package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckState implements Parcelable {
    /**
     * statetitle : 午检
     * stateid : 1
     * state : 未检测
     * statetype : 状态
     */

    private String statetitle;
    private String stateid;
    private String state;
    private String statetype;

    public String getStatetitle() {
        return statetitle;
    }

    public void setStatetitle(String statetitle) {
        this.statetitle = statetitle;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatetype() {
        return statetype;
    }

    public void setStatetype(String statetype) {
        this.statetype = statetype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.statetitle);
        dest.writeString(this.stateid);
        dest.writeString(this.state);
        dest.writeString(this.statetype);
    }

    public CheckState() {
    }

    protected CheckState(Parcel in) {
        this.statetitle = in.readString();
        this.stateid = in.readString();
        this.state = in.readString();
        this.statetype = in.readString();
    }

    public static final Parcelable.Creator<CheckState> CREATOR = new Parcelable.Creator<CheckState>() {
        @Override
        public CheckState createFromParcel(Parcel source) {
            return new CheckState(source);
        }

        @Override
        public CheckState[] newArray(int size) {
            return new CheckState[size];
        }
    };
}
