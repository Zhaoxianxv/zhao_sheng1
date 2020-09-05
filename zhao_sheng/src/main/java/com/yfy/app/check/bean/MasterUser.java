package com.yfy.app.check.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MasterUser implements Parcelable {

    /**
     * mastername : 徐晓竹
     * masterid : 120
     * masterheadpic : https://www.cdeps.sc.cn/ClientBin/head.png
     */

    private String mastername;
    private String masterid;
    private String masterheadpic;


    public String getMastername() {
        return mastername;
    }

    public void setMastername(String mastername) {
        this.mastername = mastername;
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

    public String getMasterheadpic() {
        return masterheadpic;
    }

    public void setMasterheadpic(String masterheadpic) {
        this.masterheadpic = masterheadpic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mastername);
        dest.writeString(this.masterid);
        dest.writeString(this.masterheadpic);
    }

    public MasterUser() {
    }

    protected MasterUser(Parcel in) {
        this.mastername = in.readString();
        this.masterid = in.readString();
        this.masterheadpic = in.readString();
    }

    public static final Parcelable.Creator<MasterUser> CREATOR = new Parcelable.Creator<MasterUser>() {
        @Override
        public MasterUser createFromParcel(Parcel source) {
            return new MasterUser(source);
        }

        @Override
        public MasterUser[] newArray(int size) {
            return new MasterUser[size];
        }
    };
}
