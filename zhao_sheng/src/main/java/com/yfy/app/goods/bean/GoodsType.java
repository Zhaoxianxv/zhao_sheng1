package com.yfy.app.goods.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GoodsType implements Parcelable {


    /**
     * id : 356
     * name : 话筒
     * canreply : false
     * tag : 话筒,麦克风
     * image : []
     * unit : 个
     *surpluscount":"0.00"}
     */

    private String id;
    private String name;
    private String canreply;
    private String tag;
    private String unit;
    private String surpluscount;

    public String getSurpluscount() {
        return surpluscount;
    }

    public void setSurpluscount(String surpluscount) {
        this.surpluscount = surpluscount;
    }
    //    private List<?> image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanreply() {
        return canreply;
    }

    public void setCanreply(String canreply) {
        this.canreply = canreply;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

//    public List<?> getImage() {
//        return image;
//    }
//
//    public void setImage(List<?> image) {
//        this.image = image;
//    }

    public GoodsType() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.canreply);
        dest.writeString(this.tag);
        dest.writeString(this.unit);
        dest.writeString(this.surpluscount);
    }

    protected GoodsType(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.canreply = in.readString();
        this.tag = in.readString();
        this.unit = in.readString();
        this.surpluscount = in.readString();
    }

    public static final Parcelable.Creator<GoodsType> CREATOR = new Parcelable.Creator<GoodsType>() {
        @Override
        public GoodsType createFromParcel(Parcel source) {
            return new GoodsType(source);
        }

        @Override
        public GoodsType[] newArray(int size) {
            return new GoodsType[size];
        }
    };
}
