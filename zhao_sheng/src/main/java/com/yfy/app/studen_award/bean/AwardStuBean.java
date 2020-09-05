package com.yfy.app.studen_award.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfy1 on 2017/4/10.
 */

public class AwardStuBean implements Parcelable{

    /**
     * name : 龙妙兮
     * id : 1908
     * awardinfo : []
     */

    private String name;
    private String id;
    private boolean isChick=false;




    protected AwardStuBean(Parcel in) {
        name = in.readString();
        id = in.readString();
        isChick = in.readByte() != 0;
    }

    public static final Creator<AwardStuBean> CREATOR = new Creator<AwardStuBean>() {
        @Override
        public AwardStuBean createFromParcel(Parcel in) {
            return new AwardStuBean(in);
        }

        @Override
        public AwardStuBean[] newArray(int size) {
            return new AwardStuBean[size];
        }
    };

    public boolean isChick() {
        return isChick;
    }

    public void setChick(boolean chick) {
        isChick = chick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeByte((byte) (isChick ? 1 : 0));
    }
}
