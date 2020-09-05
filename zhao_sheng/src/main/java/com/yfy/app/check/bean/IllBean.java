package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.bean.KeyValue;

public class IllBean implements Parcelable {

    private int view_type;
    private String parent_name;
    private String key;
    private String value;

    private KeyValue keyValue;

    public KeyValue getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(KeyValue keyValue) {
        this.keyValue = keyValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public IllBean(int view_type, String parent_name) {
        this.view_type = view_type;
        this.parent_name = parent_name;
    }

    public IllBean(int view_type, String key, String value) {
        this.view_type = view_type;
        this.key = key;
        this.value = value;
    }

    /**
     * tablename : 发热（体温）
     * tableid : 11
     * tablevaluetype : float_1
     * tableunit : ℃
     * tabledefaultvalue :
     * tablevaluecannull : 0
     */

    private String tablename;
    private String tableid;
    private String tablevaluetype;
    private String tableunit;
    private String tabledefaultvalue;
    private String tablevaluecannull;

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTablevaluetype() {
        return tablevaluetype;
    }

    public void setTablevaluetype(String tablevaluetype) {
        this.tablevaluetype = tablevaluetype;
    }

    public String getTableunit() {
        return tableunit;
    }

    public void setTableunit(String tableunit) {
        this.tableunit = tableunit;
    }

    public String getTabledefaultvalue() {
        return tabledefaultvalue;
    }

    public void setTabledefaultvalue(String tabledefaultvalue) {
        this.tabledefaultvalue = tabledefaultvalue;
    }

    public String getTablevaluecannull() {
        return tablevaluecannull;
    }

    public void setTablevaluecannull(String tablevaluecannull) {
        this.tablevaluecannull = tablevaluecannull;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.view_type);
        dest.writeString(this.parent_name);
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeParcelable(this.keyValue, flags);
        dest.writeString(this.tablename);
        dest.writeString(this.tableid);
        dest.writeString(this.tablevaluetype);
        dest.writeString(this.tableunit);
        dest.writeString(this.tabledefaultvalue);
        dest.writeString(this.tablevaluecannull);
    }

    protected IllBean(Parcel in) {
        this.view_type = in.readInt();
        this.parent_name = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.keyValue = in.readParcelable(KeyValue.class.getClassLoader());
        this.tablename = in.readString();
        this.tableid = in.readString();
        this.tablevaluetype = in.readString();
        this.tableunit = in.readString();
        this.tabledefaultvalue = in.readString();
        this.tablevaluecannull = in.readString();
    }

    public static final Creator<IllBean> CREATOR = new Creator<IllBean>() {
        @Override
        public IllBean createFromParcel(Parcel source) {
            return new IllBean(source);
        }

        @Override
        public IllBean[] newArray(int size) {
            return new IllBean[size];
        }
    };
}
