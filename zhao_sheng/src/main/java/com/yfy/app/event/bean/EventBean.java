package com.yfy.app.event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EventBean implements Parcelable {


    /**
     * eventid : 1
     * date : 2019/03/25
     * adddate : 2019/03/25 12:14
     * userid : 1
     * username : 管理员
     * liableuser : 测试人
     * departmentid : 1
     * departmentname : 学校文化
            */

    private String eventid;
    private String date;
    private String adddate;
    private String address;
    private String week_name;
    private String term_name;
    private String userid;
    private String username;
    private String liableuser;
    private String departmentid;
    private String departmentname;
    private String headpic;
    private String content;
    private String image;
    private boolean is=false;



    private int view_type;
    private String dep_name;

    public EventBean(int view_type, String dep_name) {
        this.view_type = view_type;
        this.dep_name = dep_name;
    }


    public String getWeek_name() {
        return week_name;
    }

    public void setWeek_name(String week_name) {
        this.week_name = week_name;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public boolean isIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLiableuser() {
        return liableuser;
    }

    public void setLiableuser(String liableuser) {
        this.liableuser = liableuser;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventid);
        dest.writeString(this.date);
        dest.writeString(this.adddate);
        dest.writeString(this.address);
        dest.writeString(this.week_name);
        dest.writeString(this.term_name);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.liableuser);
        dest.writeString(this.departmentid);
        dest.writeString(this.departmentname);
        dest.writeString(this.headpic);
        dest.writeString(this.content);
        dest.writeString(this.image);
        dest.writeByte(this.is ? (byte) 1 : (byte) 0);
        dest.writeInt(this.view_type);
        dest.writeString(this.dep_name);
    }

    protected EventBean(Parcel in) {
        this.eventid = in.readString();
        this.date = in.readString();
        this.adddate = in.readString();
        this.address = in.readString();
        this.week_name = in.readString();
        this.term_name = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.liableuser = in.readString();
        this.departmentid = in.readString();
        this.departmentname = in.readString();
        this.headpic = in.readString();
        this.content = in.readString();
        this.image = in.readString();
        this.is = in.readByte() != 0;
        this.view_type = in.readInt();
        this.dep_name = in.readString();
    }

    public static final Creator<EventBean> CREATOR = new Creator<EventBean>() {
        @Override
        public EventBean createFromParcel(Parcel source) {
            return new EventBean(source);
        }

        @Override
        public EventBean[] newArray(int size) {
            return new EventBean[size];
        }
    };
}
