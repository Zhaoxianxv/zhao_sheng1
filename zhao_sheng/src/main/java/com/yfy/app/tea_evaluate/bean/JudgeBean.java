package com.yfy.app.tea_evaluate.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class JudgeBean implements Parcelable {

    /**
     * name : 指导学生个人获奖类
     * id : 83
     */

    private String name;
    private String id;

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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
    }

    public JudgeBean() {
    }

    protected JudgeBean(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<JudgeBean> CREATOR = new Parcelable.Creator<JudgeBean>() {
        @Override
        public JudgeBean createFromParcel(Parcel source) {
            return new JudgeBean(source);
        }

        @Override
        public JudgeBean[] newArray(int size) {
            return new JudgeBean[size];
        }
    };
}
