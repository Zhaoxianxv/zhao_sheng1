package com.yfy.app.exchang.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2017/8/28.
 */

public class MyCouyseBean implements Parcelable{


    /**
     * date : 2017/8/21
     * reason : 演示
     * no : 星期1第1节
     * coursename : 语文
     * remark :
     * date1 : 2017/8/22
     * no1 : 星期2第1节
     * classname1 : 二年级一班
     * classname : 二年级一班
     * teachername : 王勇
     * id : 3026
     * state : 待审核,通过,拒绝
     * teachername1 : 陈蕾
     * coursename1 : 英语
     */
    private String date;
    private String reason;
    private String no;
    private String coursename;
    private String remark;
    private String date1;
    private String no1;
    private String classname1;
    private String classname;
    private String teachername;
    private String id;
    private String state;
    private String teachername1;
    private String coursename1;




    public void setDate(String date) {
        this.date = date;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public void setNo1(String no1) {
        this.no1 = no1;
    }

    public void setClassname1(String classname1) {
        this.classname1 = classname1;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTeachername1(String teachername1) {
        this.teachername1 = teachername1;
    }

    public void setCoursename1(String coursename1) {
        this.coursename1 = coursename1;
    }

    public String getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getNo() {
        return no;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getRemark() {
        return remark;
    }

    public String getDate1() {
        return date1;
    }

    public String getNo1() {
        return no1;
    }

    public String getClassname1() {
        return classname1;
    }

    public String getClassname() {
        return classname;
    }

    public String getTeachername() {
        return teachername;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getTeachername1() {
        return teachername1;
    }

    public String getCoursename1() {
        return coursename1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.reason);
        dest.writeString(this.no);
        dest.writeString(this.coursename);
        dest.writeString(this.remark);
        dest.writeString(this.date1);
        dest.writeString(this.no1);
        dest.writeString(this.classname1);
        dest.writeString(this.classname);
        dest.writeString(this.teachername);
        dest.writeString(this.id);
        dest.writeString(this.state);
        dest.writeString(this.teachername1);
        dest.writeString(this.coursename1);
    }

    public MyCouyseBean() {
    }

    protected MyCouyseBean(Parcel in) {
        this.date = in.readString();
        this.reason = in.readString();
        this.no = in.readString();
        this.coursename = in.readString();
        this.remark = in.readString();
        this.date1 = in.readString();
        this.no1 = in.readString();
        this.classname1 = in.readString();
        this.classname = in.readString();
        this.teachername = in.readString();
        this.id = in.readString();
        this.state = in.readString();
        this.teachername1 = in.readString();
        this.coursename1 = in.readString();
    }

    public static final Creator<MyCouyseBean> CREATOR = new Creator<MyCouyseBean>() {
        @Override
        public MyCouyseBean createFromParcel(Parcel source) {
            return new MyCouyseBean(source);
        }

        @Override
        public MyCouyseBean[] newArray(int size) {
            return new MyCouyseBean[size];
        }
    };
}
