package com.yfy.app.appointment.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.bean.TeaBean;

import java.util.List;

/**
 * Created by yfy1 on 2017/2/10.
 */

public class Rooms implements Parcelable {

    /**
     * room_id : 1
     * room_name : 阶梯教室
     */
    private String room_id;
    private String room_name;
    private String canchoose;
    private List<TeaBean> teachers;

    public List<TeaBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeaBean> teachers) {
        this.teachers = teachers;
    }

    public String getCanchoose() {
        return canchoose;
    }

    public void setCanchoose(String canchoose) {
        this.canchoose = canchoose;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.room_id);
        dest.writeString(this.room_name);
        dest.writeString(this.canchoose);
        dest.writeTypedList(this.teachers);
    }

    public Rooms() {
    }

    protected Rooms(Parcel in) {
        this.room_id = in.readString();
        this.room_name = in.readString();
        this.canchoose = in.readString();
        this.teachers = in.createTypedArrayList(TeaBean.CREATOR);
    }

    public static final Creator<Rooms> CREATOR = new Creator<Rooms>() {
        @Override
        public Rooms createFromParcel(Parcel source) {
            return new Rooms(source);
        }

        @Override
        public Rooms[] newArray(int size) {
            return new Rooms[size];
        }
    };
}
