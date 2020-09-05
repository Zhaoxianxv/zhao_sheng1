package com.yfy.app.appointment.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by yfy1 on 2017/2/10.
 */

public class MyFunRooom implements Parcelable {

    /**
     * time_name : 上午第一节
     * user_name :
     * status : 未申请
     */

    private String time_name;
    private String user_name;
    private String status;
    private boolean isSelctor=false;

    public boolean isSelctor() {
        return isSelctor;
    }

    public void setSelctor(boolean selctor) {
        isSelctor = selctor;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time_name);
        dest.writeString(this.user_name);
        dest.writeString(this.status);
        dest.writeByte(this.isSelctor ? (byte) 1 : (byte) 0);
    }

    public MyFunRooom() {
    }

    protected MyFunRooom(Parcel in) {
        this.time_name = in.readString();
        this.user_name = in.readString();
        this.status = in.readString();
        this.isSelctor = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MyFunRooom> CREATOR = new Parcelable.Creator<MyFunRooom>() {
        @Override
        public MyFunRooom createFromParcel(Parcel source) {
            return new MyFunRooom(source);
        }

        @Override
        public MyFunRooom[] newArray(int size) {
            return new MyFunRooom[size];
        }
    };
}
