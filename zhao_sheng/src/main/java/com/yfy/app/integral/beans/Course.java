package com.yfy.app.integral.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2017/9/5.
 */

public class Course implements Parcelable {

    /**
     * coursename : 语文
     * courseid : 1
     */
    private String coursename;
    private String courseid;

    public Course(String coursename, String courseid) {
        this.coursename = coursename;
        this.courseid = courseid;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getCourseid() {
        return courseid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coursename);
        dest.writeString(this.courseid);
    }

    protected Course(Parcel in) {
        this.coursename = in.readString();
        this.courseid = in.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
