package com.yfy.app.oashow.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BMaintian implements Parcelable {

    /**
     * address : 人
     * applytime : 2018/10/09 10:53
     * classid : 信息中心
     * dealimage : []
     * dealstate : 已维修
     * dealtime : 2018/10/09 00:00
     * id : 17156
     * image : []
     * isout : true
     * ladername : null
     * money :
     * nr : 员
     * outstate :
     * reslut : 完
     * username : 管理员
     */

    private String address;
    private String submit_time;
    private String dealstate;
    private String id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getDealstate() {
        return dealstate;
    }

    public void setDealstate(String dealstate) {
        this.dealstate = dealstate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.submit_time);
        dest.writeString(this.dealstate);
        dest.writeString(this.id);
    }

    public BMaintian() {
    }

    protected BMaintian(Parcel in) {
        this.address = in.readString();
        this.submit_time = in.readString();
        this.dealstate = in.readString();
        this.id = in.readString();
    }

    public static final Creator<BMaintian> CREATOR = new Creator<BMaintian>() {
        @Override
        public BMaintian createFromParcel(Parcel source) {
            return new BMaintian(source);
        }

        @Override
        public BMaintian[] newArray(int size) {
            return new BMaintian[size];
        }
    };
}
