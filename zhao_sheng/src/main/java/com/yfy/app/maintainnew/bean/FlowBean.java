package com.yfy.app.maintainnew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfyandr on 2017/12/28.
 */

public class FlowBean implements Parcelable {

    /**
     * name : 孔睿
     * avatar : http://www.cdeps.sc.cn//uploadfile/photo/20170831101647.jpg
     * time : 2017/12/28 00:00
     * state : 申请维修
     * content : 中间那一列第一个，抽屉散了。
     */
    private String name;
    private String avatar;
    private String time;
    private String state;
    private String content;
    private List<String> image;

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTime() {
        return time;
    }

    public String getState() {
        return state;
    }

    public String getContent() {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.time);
        dest.writeString(this.state);
        dest.writeString(this.content);
        dest.writeStringList(this.image);
    }

    public FlowBean() {
    }

    protected FlowBean(Parcel in) {
        this.name = in.readString();
        this.avatar = in.readString();
        this.time = in.readString();
        this.state = in.readString();
        this.content = in.readString();
        this.image = in.createStringArrayList();
    }

    public static final Parcelable.Creator<FlowBean> CREATOR = new Parcelable.Creator<FlowBean>() {
        @Override
        public FlowBean createFromParcel(Parcel source) {
            return new FlowBean(source);
        }

        @Override
        public FlowBean[] newArray(int size) {
            return new FlowBean[size];
        }
    };
}
