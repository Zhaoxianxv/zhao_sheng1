package com.yfy.app.allclass.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2017/5/9.
 */

public class Par implements Parcelable{


    private String time;
    private String id;

    public Par() {
    }

    protected Par(Parcel in) {
        time = in.readString();
        id = in.readString();
    }

    public static final Creator<Par> CREATOR = new Creator<Par>() {
        @Override
        public Par createFromParcel(Parcel in) {
            return new Par(in);
        }

        @Override
        public Par[] newArray(int size) {
            return new Par[size];
        }
    };




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time);
        parcel.writeString(id);
    }
}
