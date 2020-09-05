package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ClasslistBean implements Parcelable {
    /**
     * classname : 班级名称
     * classid : 班级id
     * 因病缺勤 : 返回具体人数如1
     * illcount : 当前生病人数
     */

    private String groupname;
    private String groupid;
    private List<CheckState> states;
    private List<IllType> illlist;

    public List<IllType> getIlllist() {
        return illlist;
    }

    public void setIlllist(List<IllType> illlist) {
        this.illlist = illlist;
    }

    public List<CheckState> getStates() {
        return states;
    }

    public void setStates(List<CheckState> states) {
        this.states = states;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public ClasslistBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupname);
        dest.writeString(this.groupid);
        dest.writeTypedList(this.states);
        dest.writeTypedList(this.illlist);
    }

    protected ClasslistBean(Parcel in) {
        this.groupname = in.readString();
        this.groupid = in.readString();
        this.states = in.createTypedArrayList(CheckState.CREATOR);
        this.illlist = in.createTypedArrayList(IllType.CREATOR);
    }

    public static final Creator<ClasslistBean> CREATOR = new Creator<ClasslistBean>() {
        @Override
        public ClasslistBean createFromParcel(Parcel source) {
            return new ClasslistBean(source);
        }

        @Override
        public ClasslistBean[] newArray(int size) {
            return new ClasslistBean[size];
        }
    };
}
