package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Teacher implements Parcelable {

    /**
     * teacherid : 175
     * teachername : 周会
     * courseid : 1
     * coursename : 语文
     * headpic : https://www.cdeps.sc.cn/ClientBin/head.png
     * isevaluated : false
     */

    private String teacherid;
    private String teachername;
    private String courseid;
    private String coursename;
    private String headpic;
    private String isevaluated;

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getIsevaluated() {
        return isevaluated;
    }

    public void setIsevaluated(String isevaluated) {
        this.isevaluated = isevaluated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teacherid);
        dest.writeString(this.teachername);
        dest.writeString(this.courseid);
        dest.writeString(this.coursename);
        dest.writeString(this.headpic);
        dest.writeString(this.isevaluated);
    }

    public Teacher() {
    }

    protected Teacher(Parcel in) {
        this.teacherid = in.readString();
        this.teachername = in.readString();
        this.courseid = in.readString();
        this.coursename = in.readString();
        this.headpic = in.readString();
        this.isevaluated = in.readString();
    }

    public static final Parcelable.Creator<Teacher> CREATOR = new Parcelable.Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel source) {
            return new Teacher(source);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };
}
