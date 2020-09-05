package com.yfy.app.notice.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class ContactsGroup implements Parcelable {

    /**
     * depid : 6
     * depname : 行政部
     */

    private String depid;
    private String depname;
    private String departid;
    private String departname;
    private String selectuser;
    private List<ChildBean> userinfob;
    private List<ChildBean> userinfo;


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

    public List<ChildBean> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<ChildBean> userinfo) {
        this.userinfo = userinfo;
    }

    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public List<ChildBean> getUserinfob() {
        return userinfob;
    }

    public void setUserinfob(List<ChildBean> userinfob) {
        this.userinfob = userinfob;
    }

    public ContactsGroup() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.depid);
        dest.writeString(this.depname);
        dest.writeString(this.departid);
        dest.writeString(this.departname);
        dest.writeString(this.selectuser);
        dest.writeTypedList(this.userinfob);
        dest.writeTypedList(this.userinfo);
    }

    protected ContactsGroup(Parcel in) {
        this.depid = in.readString();
        this.depname = in.readString();
        this.departid = in.readString();
        this.departname = in.readString();
        this.selectuser = in.readString();
        this.userinfob = in.createTypedArrayList(ChildBean.CREATOR);
        this.userinfo = in.createTypedArrayList(ChildBean.CREATOR);
    }

    public static final Creator<ContactsGroup> CREATOR = new Creator<ContactsGroup>() {
        @Override
        public ContactsGroup createFromParcel(Parcel source) {
            return new ContactsGroup(source);
        }

        @Override
        public ContactsGroup[] newArray(int size) {
            return new ContactsGroup[size];
        }
    };
}
