package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MainState implements Parcelable {

    /**
     * stateid : 1
     * statename : 申请请假
     * statecolor : ff0021ec
     */

    private int stateid;
    private String statename;
    private String statecolor;

    public int getStateid() {
        return stateid;
    }

    public void setStateid(int stateid) {
        this.stateid = stateid;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getStatecolor() {
        return statecolor;
    }

    public void setStatecolor(String statecolor) {
        this.statecolor = statecolor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stateid);
        dest.writeString(this.statename);
        dest.writeString(this.statecolor);
    }

    public MainState() {
    }

    protected MainState(Parcel in) {
        this.stateid = in.readInt();
        this.statename = in.readString();
        this.statecolor = in.readString();
    }

    public static final Parcelable.Creator<MainState> CREATOR = new Parcelable.Creator<MainState>() {
        @Override
        public MainState createFromParcel(Parcel source) {
            return new MainState(source);
        }

        @Override
        public MainState[] newArray(int size) {
            return new MainState[size];
        }
    };
}
