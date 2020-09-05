package com.yfy.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.process.bean.MainState;

import java.util.List;

public class MainValue implements Parcelable {

    /**
     * id : 1
     * title : 考勤
     * type : attendance
     * group : none
     * secondgroup : none
     */

    private int id;
    private String title;
    private int messagecount;
    private String type;
    private String group;
    private String secondgroup;
    private String icon="";
    private String right;
    private String datatype;
    private String usertype;
    private List<MainState> stateinfo;


    public int getMessagecount() {
        return messagecount;
    }

    public void setMessagecount(int messagecount) {
        this.messagecount = messagecount;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public List<MainState> getStateinfo() {
        return stateinfo;
    }

    public void setStateinfo(List<MainState> stateinfo) {
        this.stateinfo = stateinfo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSecondgroup() {
        return secondgroup;
    }

    public void setSecondgroup(String secondgroup) {
        this.secondgroup = secondgroup;
    }

    public MainValue() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.messagecount);
        dest.writeString(this.type);
        dest.writeString(this.group);
        dest.writeString(this.secondgroup);
        dest.writeString(this.icon);
        dest.writeString(this.right);
        dest.writeString(this.datatype);
        dest.writeString(this.usertype);
        dest.writeTypedList(this.stateinfo);
    }

    protected MainValue(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.messagecount = in.readInt();
        this.type = in.readString();
        this.group = in.readString();
        this.secondgroup = in.readString();
        this.icon = in.readString();
        this.right = in.readString();
        this.datatype = in.readString();
        this.usertype = in.readString();
        this.stateinfo = in.createTypedArrayList(MainState.CREATOR);
    }

    public static final Creator<MainValue> CREATOR = new Creator<MainValue>() {
        @Override
        public MainValue createFromParcel(Parcel source) {
            return new MainValue(source);
        }

        @Override
        public MainValue[] newArray(int size) {
            return new MainValue[size];
        }
    };
}
