package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.List;

public class DelayAbsenteeismClass implements Parcelable {

    /**
     * Electivename : [星期四]二年级托管一班
     * Electiveid : 638
     * Electiveaddr : 2.1班
     * stucount : 49
     * situation_count : 0
     * Elective_classdetail : []
     * Elective_stuetail : []
     */

    private int view_type=TagFinal.TYPE_ITEM;
    private String Electivename;
    private String Electiveid;
    private String Electiveaddr;
    private String stucount;
    private String situation_count;
    private String ischeck;
    private List<AbsStu> Elective_classdetail;
    private List<AbsStu> Elective_stuetail;
    private List<AbsStu> teacher_list;

    public List<AbsStu> getTeacher_list() {
        return teacher_list;
    }

    public void setTeacher_list(List<AbsStu> teacher_list) {
        this.teacher_list = teacher_list;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
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

    public String getElectiveaddr() {
        return Electiveaddr;
    }

    public void setElectiveaddr(String Electiveaddr) {
        this.Electiveaddr = Electiveaddr;
    }

    public String getStucount() {
        return stucount;
    }

    public void setStucount(String stucount) {
        this.stucount = stucount;
    }

    public String getSituation_count() {
        return situation_count;
    }

    public void setSituation_count(String situation_count) {
        this.situation_count = situation_count;
    }

    public List<AbsStu> getElective_stuetail() {
        return Elective_stuetail;
    }

    public void setElective_stuetail(List<AbsStu> elective_stuetail) {
        Elective_stuetail = elective_stuetail;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }


    public List<AbsStu> getElective_classdetail() {
        return Elective_classdetail;
    }

    public void setElective_classdetail(List<AbsStu> elective_classdetail) {
        Elective_classdetail = elective_classdetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.view_type);
        dest.writeString(this.Electivename);
        dest.writeString(this.Electiveid);
        dest.writeString(this.Electiveaddr);
        dest.writeString(this.stucount);
        dest.writeString(this.situation_count);
        dest.writeString(this.ischeck);
        dest.writeTypedList(this.Elective_classdetail);
        dest.writeTypedList(this.Elective_stuetail);
        dest.writeTypedList(this.teacher_list);
    }

    public DelayAbsenteeismClass() {
    }

    protected DelayAbsenteeismClass(Parcel in) {
        this.view_type = in.readInt();
        this.Electivename = in.readString();
        this.Electiveid = in.readString();
        this.Electiveaddr = in.readString();
        this.stucount = in.readString();
        this.situation_count = in.readString();
        this.ischeck = in.readString();
        this.Elective_classdetail = in.createTypedArrayList(AbsStu.CREATOR);
        this.Elective_stuetail = in.createTypedArrayList(AbsStu.CREATOR);
        this.teacher_list = in.createTypedArrayList(AbsStu.CREATOR);
    }

    public static final Parcelable.Creator<DelayAbsenteeismClass> CREATOR = new Parcelable.Creator<DelayAbsenteeismClass>() {
        @Override
        public DelayAbsenteeismClass createFromParcel(Parcel source) {
            return new DelayAbsenteeismClass(source);
        }

        @Override
        public DelayAbsenteeismClass[] newArray(int size) {
            return new DelayAbsenteeismClass[size];
        }
    };
}
