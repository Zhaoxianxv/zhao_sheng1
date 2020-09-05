package com.yfy.app.attennew.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2018/1/4.
 */

public class AttenFlow implements Parcelable {

    /**
     * name : 管理员
     * avatar : http://www.cdeps.sc.cn//uploadfile/photo/20171222112306.jpg
     * time : 2018/01/03 10:57
     * state : 申请请假
     * content : 123
     */
    private String name;
    private String avatar;
    private String time;
    private String state;
    private String content;

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
    }

    public AttenFlow() {
    }

    protected AttenFlow(Parcel in) {
        this.name = in.readString();
        this.avatar = in.readString();
        this.time = in.readString();
        this.state = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<AttenFlow> CREATOR = new Parcelable.Creator<AttenFlow>() {
        @Override
        public AttenFlow createFromParcel(Parcel source) {
            return new AttenFlow(source);
        }

        @Override
        public AttenFlow[] newArray(int size) {
            return new AttenFlow[size];
        }
    };
}
