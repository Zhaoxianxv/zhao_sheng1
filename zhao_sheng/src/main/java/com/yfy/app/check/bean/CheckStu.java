package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckStu implements Parcelable {

//-------------------stu
    private String username;
    private String userid;
    private String headpic;
    private String userheadpic;
    private String mobile;
    private String phonenumber;
    private String classid;
    private String classname;
    private String isabsent;







//		"username": "学生3",
//		"userid": "7382",
//		"headpic": "https://www.cdeps.sc.cn/ClientBin/head.png",
//		"mobile": "",
//		"illstate": "正常",
//		"illid": "0"
    /**
     * stumobile :
     * illstate : 正常
     * illid : 0
     */
    private String illtype;
    private String stumobile;
    private String illstate;
    private String illid;
    private String illcontent;
    private boolean isCheck=false;
    private int span_size=4;


    public String getIllcontent() {
        return illcontent;
    }

    public void setIllcontent(String illcontent) {
        this.illcontent = illcontent;
    }

    public String getIlltype() {
        return illtype;
    }

    public void setIlltype(String illtype) {
        this.illtype = illtype;
    }

    public String getUserheadpic() {
        return userheadpic;
    }

    public void setUserheadpic(String userheadpic) {
        this.userheadpic = userheadpic;
    }

    public String getStumobile() {
        return stumobile;
    }

    public void setStumobile(String stumobile) {
        this.stumobile = stumobile;
    }

    public String getIllstate() {
        return illstate;
    }

    public void setIllstate(String illstate) {
        this.illstate = illstate;
    }

    public String getIllid() {
        return illid;
    }

    public void setIllid(String illid) {
        this.illid = illid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSpan_size() {
        return span_size;
    }

    public void setSpan_size(int span_size) {
        this.span_size = span_size;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getIsabsent() {
        return isabsent;
    }

    public void setIsabsent(String isabsent) {
        this.isabsent = isabsent;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public CheckStu() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.userid);
        dest.writeString(this.headpic);
        dest.writeString(this.userheadpic);
        dest.writeString(this.mobile);
        dest.writeString(this.phonenumber);
        dest.writeString(this.classid);
        dest.writeString(this.classname);
        dest.writeString(this.isabsent);
        dest.writeString(this.illtype);
        dest.writeString(this.stumobile);
        dest.writeString(this.illstate);
        dest.writeString(this.illid);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.span_size);
    }

    protected CheckStu(Parcel in) {
        this.username = in.readString();
        this.userid = in.readString();
        this.headpic = in.readString();
        this.userheadpic = in.readString();
        this.mobile = in.readString();
        this.phonenumber = in.readString();
        this.classid = in.readString();
        this.classname = in.readString();
        this.isabsent = in.readString();
        this.illtype = in.readString();
        this.stumobile = in.readString();
        this.illstate = in.readString();
        this.illid = in.readString();
        this.isCheck = in.readByte() != 0;
        this.span_size = in.readInt();
    }

    public static final Creator<CheckStu> CREATOR = new Creator<CheckStu>() {
        @Override
        public CheckStu createFromParcel(Parcel source) {
            return new CheckStu(source);
        }

        @Override
        public CheckStu[] newArray(int size) {
            return new CheckStu[size];
        }
    };
}
