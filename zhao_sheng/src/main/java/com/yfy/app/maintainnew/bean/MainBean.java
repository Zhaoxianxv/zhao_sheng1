package com.yfy.app.maintainnew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/28.
 */

public class MainBean implements Parcelable {


    /**
     * user_avatar : http://www.cdeps.sc.cn//uploadfile/photo/20170831101647.jpg
     * address : 二四班
     * submit_time : 2017/12/28 00:00
     * user_name : 孔睿
     * department_name : 后勤部
     * id : 16214
     * content : 中间那一列第一个，抽屉散了。
     * maintain_time :
     */
    private String user_avatar;
    private String address;
    private String submit_time;
    private String user_name;
    private String department_name;
    private String id;
    private String content;
    private String mobile;
    private String maintain_time;
    private String dealstate;
    private List<String> image;
    private List<FlowBean> maintain_info;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDealstate() {
        return dealstate;
    }

    public void setDealstate(String dealstate) {
        this.dealstate = dealstate;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public List<FlowBean> getMaintain_info() {
        return maintain_info;
    }

    public void setMaintain_info(List<FlowBean> maintain_info) {
        this.maintain_info = maintain_info;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMaintain_time(String maintain_time) {
        this.maintain_time = maintain_time;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getAddress() {
        return address;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getMaintain_time() {
        return maintain_time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_avatar);
        dest.writeString(this.address);
        dest.writeString(this.submit_time);
        dest.writeString(this.user_name);
        dest.writeString(this.department_name);
        dest.writeString(this.id);
        dest.writeString(this.content);
        dest.writeString(this.mobile);
        dest.writeString(this.maintain_time);
        dest.writeString(this.dealstate);
        dest.writeStringList(this.image);
        dest.writeTypedList(this.maintain_info);
    }

    public MainBean() {
    }

    protected MainBean(Parcel in) {
        this.user_avatar = in.readString();
        this.address = in.readString();
        this.submit_time = in.readString();
        this.user_name = in.readString();
        this.department_name = in.readString();
        this.id = in.readString();
        this.content = in.readString();
        this.mobile = in.readString();
        this.maintain_time = in.readString();
        this.dealstate = in.readString();
        this.image = in.createStringArrayList();
        this.maintain_info = in.createTypedArrayList(FlowBean.CREATOR);
    }

    public static final Parcelable.Creator<MainBean> CREATOR = new Parcelable.Creator<MainBean>() {
        @Override
        public MainBean createFromParcel(Parcel source) {
            return new MainBean(source);
        }

        @Override
        public MainBean[] newArray(int size) {
            return new MainBean[size];
        }
    };
}
