package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProcessMainBean implements Parcelable {

    /**
     * id : 5.0
     * HeadPic : /uploadfile/files/20200803170800726.jpg
     * title : 猜猜猜
     * stateid : 2
     * state : 申请请假
     * adddate : 2020/07/17 15:21
     * realname : 管理员
     */

    private double id;
    private String HeadPic;
    private String title;
    private String stateid;
    private String state;
    private String adddate;
    private String statecolor;
    private String realname;

    public String getStatecolor() {
        return statecolor;
    }

    public void setStatecolor(String statecolor) {
        this.statecolor = statecolor;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.id);
        dest.writeString(this.HeadPic);
        dest.writeString(this.title);
        dest.writeString(this.stateid);
        dest.writeString(this.state);
        dest.writeString(this.adddate);
        dest.writeString(this.realname);
    }

    public ProcessMainBean() {
    }

    protected ProcessMainBean(Parcel in) {
        this.id = in.readDouble();
        this.HeadPic = in.readString();
        this.title = in.readString();
        this.stateid = in.readString();
        this.state = in.readString();
        this.adddate = in.readString();
        this.realname = in.readString();
    }

    public static final Parcelable.Creator<ProcessMainBean> CREATOR = new Parcelable.Creator<ProcessMainBean>() {
        @Override
        public ProcessMainBean createFromParcel(Parcel source) {
            return new ProcessMainBean(source);
        }

        @Override
        public ProcessMainBean[] newArray(int size) {
            return new ProcessMainBean[size];
        }
    };
}
