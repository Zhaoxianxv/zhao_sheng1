package com.yfy.app.goods.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodNumBean implements Parcelable {

    /**
     * id : 1
     * item_id : 434
     * item_name : 按动式中性笔黑
     * new_count : 10
     * now_count : 0.00
     * state : 待审核
     * adddate : 2020/03/26 16:15
     * addluser : 管理员
     * adduserheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * Approvaldata : 0.00
     * Approvaluser : 0.00
     * Approvaluserheadpic : 0.00
     */

    private String id;
    private String item_id;
    private String item_name;
    private String new_count;
    private String now_count;
    private String state;
    private String adddate;
    private String addluser;
    private String adduserheadpic;
    private String Approvaldata;
    private String Approvaluser;
    private String Approvaluserheadpic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getNew_count() {
        return new_count;
    }

    public void setNew_count(String new_count) {
        this.new_count = new_count;
    }

    public String getNow_count() {
        return now_count;
    }

    public void setNow_count(String now_count) {
        this.now_count = now_count;
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

    public String getAddluser() {
        return addluser;
    }

    public void setAddluser(String addluser) {
        this.addluser = addluser;
    }

    public String getAdduserheadpic() {
        return adduserheadpic;
    }

    public void setAdduserheadpic(String adduserheadpic) {
        this.adduserheadpic = adduserheadpic;
    }

    public String getApprovaldata() {
        return Approvaldata;
    }

    public void setApprovaldata(String Approvaldata) {
        this.Approvaldata = Approvaldata;
    }

    public String getApprovaluser() {
        return Approvaluser;
    }

    public void setApprovaluser(String Approvaluser) {
        this.Approvaluser = Approvaluser;
    }

    public String getApprovaluserheadpic() {
        return Approvaluserheadpic;
    }

    public void setApprovaluserheadpic(String Approvaluserheadpic) {
        this.Approvaluserheadpic = Approvaluserheadpic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.item_id);
        dest.writeString(this.item_name);
        dest.writeString(this.new_count);
        dest.writeString(this.now_count);
        dest.writeString(this.state);
        dest.writeString(this.adddate);
        dest.writeString(this.addluser);
        dest.writeString(this.adduserheadpic);
        dest.writeString(this.Approvaldata);
        dest.writeString(this.Approvaluser);
        dest.writeString(this.Approvaluserheadpic);
    }

    public GoodNumBean() {
    }

    protected GoodNumBean(Parcel in) {
        this.id = in.readString();
        this.item_id = in.readString();
        this.item_name = in.readString();
        this.new_count = in.readString();
        this.now_count = in.readString();
        this.state = in.readString();
        this.adddate = in.readString();
        this.addluser = in.readString();
        this.adduserheadpic = in.readString();
        this.Approvaldata = in.readString();
        this.Approvaluser = in.readString();
        this.Approvaluserheadpic = in.readString();
    }

    public static final Parcelable.Creator<GoodNumBean> CREATOR = new Parcelable.Creator<GoodNumBean>() {
        @Override
        public GoodNumBean createFromParcel(Parcel source) {
            return new GoodNumBean(source);
        }

        @Override
        public GoodNumBean[] newArray(int size) {
            return new GoodNumBean[size];
        }
    };
}
