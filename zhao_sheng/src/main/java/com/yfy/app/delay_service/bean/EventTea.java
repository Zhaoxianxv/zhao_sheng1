package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class EventTea implements Parcelable {


    /**
     * teachername : 何洪丽
     * teacherid : 2281
     * phonenumber :
     * headpic : https://www.cdpxjj.com/ClientBin/head.png
     * Electivestucount : 0
     * Electivestuarrive : 0
     * Electivestuarrivebytea : 0
     */

    private String teachername;
    private String teacherid;
    private String phonenumber;
    private String headpic;
    private String Electivestucount;
    private String Electivestuarrive;
    private String Electivestuarrivebytea;

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getElectivestucount() {
        return Electivestucount;
    }

    public void setElectivestucount(String Electivestucount) {
        this.Electivestucount = Electivestucount;
    }

    public String getElectivestuarrive() {
        return Electivestuarrive;
    }

    public void setElectivestuarrive(String Electivestuarrive) {
        this.Electivestuarrive = Electivestuarrive;
    }

    public String getElectivestuarrivebytea() {
        return Electivestuarrivebytea;
    }

    public void setElectivestuarrivebytea(String Electivestuarrivebytea) {
        this.Electivestuarrivebytea = Electivestuarrivebytea;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teachername);
        dest.writeString(this.teacherid);
        dest.writeString(this.phonenumber);
        dest.writeString(this.headpic);
        dest.writeString(this.Electivestucount);
        dest.writeString(this.Electivestuarrive);
        dest.writeString(this.Electivestuarrivebytea);
    }

    public EventTea() {
    }

    protected EventTea(Parcel in) {
        this.teachername = in.readString();
        this.teacherid = in.readString();
        this.phonenumber = in.readString();
        this.headpic = in.readString();
        this.Electivestucount = in.readString();
        this.Electivestuarrive = in.readString();
        this.Electivestuarrivebytea = in.readString();
    }

    public static final Creator<EventTea> CREATOR = new Creator<EventTea>() {
        @Override
        public EventTea createFromParcel(Parcel source) {
            return new EventTea(source);
        }

        @Override
        public EventTea[] newArray(int size) {
            return new EventTea[size];
        }
    };
}
