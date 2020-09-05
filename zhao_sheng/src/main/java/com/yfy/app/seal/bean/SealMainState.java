package com.yfy.app.seal.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SealMainState implements Parcelable {

    /**
     * statusid : 0
     * content : 申请盖章
     * state : 待审核
     * adddate : 2020/04/22
     * addusername : 管理员
     * adduserid : 1
     * adduserheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     */

    private String statusid;
    private String content;
    private String state;
    private String adddate;
    private String addusername;
    private String adduserid;
    private String adduserheadpic;

    public String getStatusid() {
        return statusid;
    }

    public void setStatusid(String statusid) {
        this.statusid = statusid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getAddusername() {
        return addusername;
    }

    public void setAddusername(String addusername) {
        this.addusername = addusername;
    }

    public String getAdduserid() {
        return adduserid;
    }

    public void setAdduserid(String adduserid) {
        this.adduserid = adduserid;
    }

    public String getAdduserheadpic() {
        return adduserheadpic;
    }

    public void setAdduserheadpic(String adduserheadpic) {
        this.adduserheadpic = adduserheadpic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.statusid);
        dest.writeString(this.content);
        dest.writeString(this.state);
        dest.writeString(this.adddate);
        dest.writeString(this.addusername);
        dest.writeString(this.adduserid);
        dest.writeString(this.adduserheadpic);
    }

    public SealMainState() {
    }

    protected SealMainState(Parcel in) {
        this.statusid = in.readString();
        this.content = in.readString();
        this.state = in.readString();
        this.adddate = in.readString();
        this.addusername = in.readString();
        this.adduserid = in.readString();
        this.adduserheadpic = in.readString();
    }

    public static final Creator<SealMainState> CREATOR = new Creator<SealMainState>() {
        @Override
        public SealMainState createFromParcel(Parcel source) {
            return new SealMainState(source);
        }

        @Override
        public SealMainState[] newArray(int size) {
            return new SealMainState[size];
        }
    };
}
