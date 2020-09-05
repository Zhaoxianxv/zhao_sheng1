package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Stu implements Parcelable {

    /**
     * stuid : 1908
     * stuname : 龙妙兮
     * headpic : https://www.cdeps.sc.cn/ClientBin/head.png
     */

    private String stuid;
    private String stuname;
    private String headpic;

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stuid);
        dest.writeString(this.stuname);
        dest.writeString(this.headpic);
    }

    public Stu() {
    }

    protected Stu(Parcel in) {
        this.stuid = in.readString();
        this.stuname = in.readString();
        this.headpic = in.readString();
    }

    public static final Parcelable.Creator<Stu> CREATOR = new Parcelable.Creator<Stu>() {
        @Override
        public Stu createFromParcel(Parcel source) {
            return new Stu(source);
        }

        @Override
        public Stu[] newArray(int size) {
            return new Stu[size];
        }
    };
}
