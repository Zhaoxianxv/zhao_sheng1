package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.List;

public class AbsentInfo implements Parcelable {


    private String gradename;
    private String gradeid;


    /**
     * Electivename : 测试1
     * Electiveid : 1590
     * Elective_classdetail : []
     */


    private int view_type;
    private boolean expand=false;
    private String Electivename;
    private String Electiveid;
    private String Electiveaddr;
    private String arrive_count;
    private List<EventClass> Elective_classdetail;
    private List<EventClass> Elective_stuetail;
    private List<EventTea> teacher_list;


    public String getArrive_count() {
        return arrive_count;
    }

    public void setArrive_count(String arrive_count) {
        this.arrive_count = arrive_count;
    }

    public String getElectiveaddr() {
        return Electiveaddr;
    }

    public void setElectiveaddr(String electiveaddr) {
        Electiveaddr = electiveaddr;
    }

    public List<EventTea> getTeacher_list() {
        return teacher_list;
    }

    public void setTeacher_list(List<EventTea> teacher_list) {
        this.teacher_list = teacher_list;
    }

    public List<EventClass> getElective_stuetail() {
        return Elective_stuetail;
    }

    public void setElective_stuetail(List<EventClass> elective_stuetail) {
        Elective_stuetail = elective_stuetail;
    }

    public String getElectivename() {
        return Electivename;
    }

    public void setElectivename(String Electivename) {
        this.Electivename = Electivename;
    }

    public String getElectiveid() {
        return Electiveid;
    }

    public void setElectiveid(String Electiveid) {
        this.Electiveid = Electiveid;
    }

    public List<EventClass> getElective_classdetail() {
        return Elective_classdetail;
    }

    public void setElective_classdetail(List<EventClass> Elective_classdetail) {
        this.Elective_classdetail = Elective_classdetail;
    }


    public String getGradename() {

        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gradename);
        dest.writeString(this.gradeid);
        dest.writeInt(this.view_type);
        dest.writeByte(this.expand ? (byte) 1 : (byte) 0);
        dest.writeString(this.Electivename);
        dest.writeString(this.Electiveid);
        dest.writeString(this.Electiveaddr);
        dest.writeString(this.arrive_count);
        dest.writeTypedList(this.Elective_classdetail);
        dest.writeTypedList(this.Elective_stuetail);
        dest.writeTypedList(this.teacher_list);
    }

    public AbsentInfo() {
    }

    protected AbsentInfo(Parcel in) {
        this.gradename = in.readString();
        this.gradeid = in.readString();
        this.view_type = in.readInt();
        this.expand = in.readByte() != 0;
        this.Electivename = in.readString();
        this.Electiveid = in.readString();
        this.Electiveaddr = in.readString();
        this.arrive_count = in.readString();
        this.Elective_classdetail = in.createTypedArrayList(EventClass.CREATOR);
        this.Elective_stuetail = in.createTypedArrayList(EventClass.CREATOR);
        this.teacher_list = in.createTypedArrayList(EventTea.CREATOR);
    }

    public static final Creator<AbsentInfo> CREATOR = new Creator<AbsentInfo>() {
        @Override
        public AbsentInfo createFromParcel(Parcel source) {
            return new AbsentInfo(source);
        }

        @Override
        public AbsentInfo[] newArray(int size) {
            return new AbsentInfo[size];
        }
    };
}
