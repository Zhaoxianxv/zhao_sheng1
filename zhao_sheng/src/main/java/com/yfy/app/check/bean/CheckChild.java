package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class CheckChild implements Parcelable {

    private int view_type=TagFinal.TYPE_ITEM;

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    private boolean is_show=false;

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    //------------parent=-----------
    private String illid;
    private String username;
    private String userid;
    private String state;
    private String userheadpic;
    private String usermobile;
    private String illdate;
    private String illcheckdate;
    private String illchecktype;
    private String illtype;
    private String adduser;
    private String adduserheadpic;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserheadpic() {
        return userheadpic;
    }

    public void setUserheadpic(String userheadpic) {
        this.userheadpic = userheadpic;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getIlldate() {
        return illdate;
    }

    public void setIlldate(String illdate) {
        this.illdate = illdate;
    }

    public String getIllcheckdate() {
        return illcheckdate;
    }

    public void setIllcheckdate(String illcheckdate) {
        this.illcheckdate = illcheckdate;
    }

    public String getIllchecktype() {
        return illchecktype;
    }

    public void setIllchecktype(String illchecktype) {
        this.illchecktype = illchecktype;
    }

    public String getIlltype() {
        return illtype;
    }

    public void setIlltype(String illtype) {
        this.illtype = illtype;
    }

    public String getAdduser() {
        return adduser;
    }

    public void setAdduser(String adduser) {
        this.adduser = adduser;
    }

    public String getAdduserheadpic() {
        return adduserheadpic;
    }

    public void setAdduserheadpic(String adduserheadpic) {
        this.adduserheadpic = adduserheadpic;
    }

    /**
     * seekid : 1
     * adddate : 2020/03/31 16:30
     * ----------------child-----------------
     */

    private String seekid;
    private String adddate;
    private List<CheckChildBean> illstatedetail;

    public List<CheckChildBean> getIllstatedetail() {
        return illstatedetail;
    }

    public void setIllstatedetail(List<CheckChildBean> illstatedetail) {
        this.illstatedetail = illstatedetail;
    }

    public String getSeekid() {
        return seekid;
    }

    public void setSeekid(String seekid) {
        this.seekid = seekid;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public CheckChild() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.view_type);
        dest.writeByte(this.is_show ? (byte) 1 : (byte) 0);
        dest.writeString(this.illid);
        dest.writeString(this.username);
        dest.writeString(this.userid);
        dest.writeString(this.state);
        dest.writeString(this.userheadpic);
        dest.writeString(this.usermobile);
        dest.writeString(this.illdate);
        dest.writeString(this.illcheckdate);
        dest.writeString(this.illchecktype);
        dest.writeString(this.illtype);
        dest.writeString(this.adduser);
        dest.writeString(this.adduserheadpic);
        dest.writeString(this.seekid);
        dest.writeString(this.adddate);
        dest.writeList(this.illstatedetail);
    }

    protected CheckChild(Parcel in) {
        this.view_type = in.readInt();
        this.is_show = in.readByte() != 0;
        this.illid = in.readString();
        this.username = in.readString();
        this.userid = in.readString();
        this.state = in.readString();
        this.userheadpic = in.readString();
        this.usermobile = in.readString();
        this.illdate = in.readString();
        this.illcheckdate = in.readString();
        this.illchecktype = in.readString();
        this.illtype = in.readString();
        this.adduser = in.readString();
        this.adduserheadpic = in.readString();
        this.seekid = in.readString();
        this.adddate = in.readString();
        this.illstatedetail = new ArrayList<CheckChildBean>();
        in.readList(this.illstatedetail, CheckChildBean.class.getClassLoader());
    }

    public static final Creator<CheckChild> CREATOR = new Creator<CheckChild>() {
        @Override
        public CheckChild createFromParcel(Parcel source) {
            return new CheckChild(source);
        }

        @Override
        public CheckChild[] newArray(int size) {
            return new CheckChild[size];
        }
    };
}
