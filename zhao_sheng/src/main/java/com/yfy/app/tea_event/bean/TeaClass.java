package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TeaClass implements Parcelable {

    /**
     * courseid : 2
     * coursename : 数学
     * classname : 四年级一班
     * classid : 51
     */

    private String courseid;
    private String coursename;
    private String classname;
    private String classid;
    private List<TeaDeInfo> checkresault;


    public List<TeaDeInfo> getCheckresault() {
        return checkresault;
    }

    public void setCheckresault(List<TeaDeInfo> checkresault) {
        this.checkresault = checkresault;
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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseid);
        dest.writeString(this.coursename);
        dest.writeString(this.classname);
        dest.writeString(this.classid);
        dest.writeTypedList(this.checkresault);
    }

    public TeaClass() {
    }

    protected TeaClass(Parcel in) {
        this.courseid = in.readString();
        this.coursename = in.readString();
        this.classname = in.readString();
        this.classid = in.readString();
        this.checkresault = in.createTypedArrayList(TeaDeInfo.CREATOR);
    }

    public static final Parcelable.Creator<TeaClass> CREATOR = new Parcelable.Creator<TeaClass>() {
        @Override
        public TeaClass createFromParcel(Parcel source) {
            return new TeaClass(source);
        }

        @Override
        public TeaClass[] newArray(int size) {
            return new TeaClass[size];
        }
    };
}
