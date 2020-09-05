package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class IllType implements Parcelable {

    /**
     * illtypename : 传染病
     * illtypeid : 7
     * illtypecount : 0
     *   "illtypename": "传染病",
     *       "illtypeid": "7",
     */
    private String illtypegroupname;
    private int view_type;

    public String getIlltypegroupname() {
        return illtypegroupname;
    }

    public void setIlltypegroupname(String illtypegroupname) {
        this.illtypegroupname = illtypegroupname;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    private String inspecttypename;
    private String inspecttypeid;
    private String illtypename;
    private String illtypeid;
    private String illtypecount;

    private List<IllBean> illtypetable;


    public String getInspecttypename() {
        return inspecttypename;
    }

    public void setInspecttypename(String inspecttypename) {
        this.inspecttypename = inspecttypename;
    }

    public String getInspecttypeid() {
        return inspecttypeid;
    }

    public void setInspecttypeid(String inspecttypeid) {
        this.inspecttypeid = inspecttypeid;
    }

    public List<IllBean> getIlltypetable() {
        return illtypetable;
    }

    public void setIlltypetable(List<IllBean> illtypetable) {
        this.illtypetable = illtypetable;
    }


    public String getIlltypename() {
        return illtypename;
    }

    public void setIlltypename(String illtypename) {
        this.illtypename = illtypename;
    }

    public String getIlltypeid() {
        return illtypeid;
    }

    public void setIlltypeid(String illtypeid) {
        this.illtypeid = illtypeid;
    }

    public String getIlltypecount() {
        return illtypecount;
    }

    public void setIlltypecount(String illtypecount) {
        this.illtypecount = illtypecount;
    }

    public IllType() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inspecttypename);
        dest.writeString(this.inspecttypeid);
        dest.writeString(this.illtypename);
        dest.writeString(this.illtypeid);
        dest.writeString(this.illtypecount);
        dest.writeTypedList(this.illtypetable);
    }

    protected IllType(Parcel in) {
        this.inspecttypename = in.readString();
        this.inspecttypeid = in.readString();
        this.illtypename = in.readString();
        this.illtypeid = in.readString();
        this.illtypecount = in.readString();
        this.illtypetable = in.createTypedArrayList(IllBean.CREATOR);
    }

    public static final Creator<IllType> CREATOR = new Creator<IllType>() {
        @Override
        public IllType createFromParcel(Parcel source) {
            return new IllType(source);
        }

        @Override
        public IllType[] newArray(int size) {
            return new IllType[size];
        }
    };
}
