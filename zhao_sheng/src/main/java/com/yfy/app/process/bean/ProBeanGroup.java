package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProBeanGroup implements Parcelable {

    /**
     * depid : 6
     * depname : 行政部
     */

    private String departid;
    private String departname;
    private String selectuser;
    private List<ProBeanChild> userinfo;


    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public String getSelectuser() {
        return selectuser;
    }

    public void setSelectuser(String selectuser) {
        this.selectuser = selectuser;
    }

    public List<ProBeanChild> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<ProBeanChild> userinfo) {
        this.userinfo = userinfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departid);
        dest.writeString(this.departname);
        dest.writeString(this.selectuser);
        dest.writeTypedList(this.userinfo);
    }

    public ProBeanGroup() {
    }

    protected ProBeanGroup(Parcel in) {
        this.departid = in.readString();
        this.departname = in.readString();
        this.selectuser = in.readString();
        this.userinfo = in.createTypedArrayList(ProBeanChild.CREATOR);
    }

    public static final Creator<ProBeanGroup> CREATOR = new Creator<ProBeanGroup>() {
        @Override
        public ProBeanGroup createFromParcel(Parcel source) {
            return new ProBeanGroup(source);
        }

        @Override
        public ProBeanGroup[] newArray(int size) {
            return new ProBeanGroup[size];
        }
    };
}
