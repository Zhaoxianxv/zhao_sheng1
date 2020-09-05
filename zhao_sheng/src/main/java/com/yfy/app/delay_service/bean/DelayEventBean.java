package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DelayEventBean implements Parcelable {

    /**
     * adminname : 管理员
     * adminheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * Electivedetail : 测试
     * leavedetail : 测试
     * isaddbyme : true
     */

    private String adminname;
    private String adminheadpic;
    private String Electivedetail;
    private String leavedetail;
    private String isaddbyme;
    private String Electivedetailpic;
    private String leavedetailpic;

    public String getElectivedetailpic() {
        return Electivedetailpic;
    }

    public void setElectivedetailpic(String electivedetailpic) {
        Electivedetailpic = electivedetailpic;
    }

    public String getLeavedetailpic() {
        return leavedetailpic;
    }

    public void setLeavedetailpic(String leavedetailpic) {
        this.leavedetailpic = leavedetailpic;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminheadpic() {
        return adminheadpic;
    }

    public void setAdminheadpic(String adminheadpic) {
        this.adminheadpic = adminheadpic;
    }

    public String getElectivedetail() {
        return Electivedetail;
    }

    public void setElectivedetail(String Electivedetail) {
        this.Electivedetail = Electivedetail;
    }

    public String getLeavedetail() {
        return leavedetail;
    }

    public void setLeavedetail(String leavedetail) {
        this.leavedetail = leavedetail;
    }

    public String getIsaddbyme() {
        return isaddbyme;
    }

    public void setIsaddbyme(String isaddbyme) {
        this.isaddbyme = isaddbyme;
    }

    public DelayEventBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.adminname);
        dest.writeString(this.adminheadpic);
        dest.writeString(this.Electivedetail);
        dest.writeString(this.leavedetail);
        dest.writeString(this.isaddbyme);
        dest.writeString(this.Electivedetailpic);
        dest.writeString(this.leavedetailpic);
    }

    protected DelayEventBean(Parcel in) {
        this.adminname = in.readString();
        this.adminheadpic = in.readString();
        this.Electivedetail = in.readString();
        this.leavedetail = in.readString();
        this.isaddbyme = in.readString();
        this.Electivedetailpic = in.readString();
        this.leavedetailpic = in.readString();
    }

    public static final Creator<DelayEventBean> CREATOR = new Creator<DelayEventBean>() {
        @Override
        public DelayEventBean createFromParcel(Parcel source) {
            return new DelayEventBean(source);
        }

        @Override
        public DelayEventBean[] newArray(int size) {
            return new DelayEventBean[size];
        }
    };
}
