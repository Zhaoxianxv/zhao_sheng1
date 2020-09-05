package com.yfy.app.appointment.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2018/5/25.
 */

public class RoomType implements Parcelable {

    /**
     * id : 1
     * room : 班级活动
     */

    private String id;
    private String room;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.room);
    }

    public RoomType() {
    }

    protected RoomType(Parcel in) {
        this.id = in.readString();
        this.room = in.readString();
    }

    public static final Parcelable.Creator<RoomType> CREATOR = new Parcelable.Creator<RoomType>() {
        @Override
        public RoomType createFromParcel(Parcel source) {
            return new RoomType(source);
        }

        @Override
        public RoomType[] newArray(int size) {
            return new RoomType[size];
        }
    };
}
