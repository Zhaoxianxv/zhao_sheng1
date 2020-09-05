package com.yfy.app.seal.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SealTable implements Parcelable {

    /**
     * tableid : 4
     * tablename : 事由
     * defaultvalue :
     * valuetype : longtxt
     * value : 哦
     */

    private String tableid;
    private String tablename;
    private String defaultvalue;
    private String valuetype;
    private String value;

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tableid);
        dest.writeString(this.tablename);
        dest.writeString(this.defaultvalue);
        dest.writeString(this.valuetype);
        dest.writeString(this.value);
    }

    public SealTable() {
    }

    protected SealTable(Parcel in) {
        this.tableid = in.readString();
        this.tablename = in.readString();
        this.defaultvalue = in.readString();
        this.valuetype = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<SealTable> CREATOR = new Parcelable.Creator<SealTable>() {
        @Override
        public SealTable createFromParcel(Parcel source) {
            return new SealTable(source);
        }

        @Override
        public SealTable[] newArray(int size) {
            return new SealTable[size];
        }
    };
}
