package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ProOperation implements Parcelable {

    /**
     * operationname : 同意请假
     * contenttype : content
     * operationtype : setstate_2
     */

    private String operationname;
    private String contenttype;
    private String operationtype;

    public String getOperationname() {
        return operationname;
    }

    public void setOperationname(String operationname) {
        this.operationname = operationname;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getOperationtype() {
        return operationtype;
    }

    public void setOperationtype(String operationtype) {
        this.operationtype = operationtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.operationname);
        dest.writeString(this.contenttype);
        dest.writeString(this.operationtype);
    }

    public ProOperation() {
    }

    protected ProOperation(Parcel in) {
        this.operationname = in.readString();
        this.contenttype = in.readString();
        this.operationtype = in.readString();
    }

    public static final Parcelable.Creator<ProOperation> CREATOR = new Parcelable.Creator<ProOperation>() {
        @Override
        public ProOperation createFromParcel(Parcel source) {
            return new ProOperation(source);
        }

        @Override
        public ProOperation[] newArray(int size) {
            return new ProOperation[size];
        }
    };
}
