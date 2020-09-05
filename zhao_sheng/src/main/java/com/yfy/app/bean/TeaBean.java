package com.yfy.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TeaBean implements Parcelable {


    /**
     * teacherid : 1
     * teachername : 管理员
     * teacherheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20190704033741.jpg
     */

    private String teacherid;
    private String teachername;
    private String teacherheadpic;

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacherheadpic() {
        return teacherheadpic;
    }

    public void setTeacherheadpic(String teacherheadpic) {
        this.teacherheadpic = teacherheadpic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teacherid);
        dest.writeString(this.teachername);
        dest.writeString(this.teacherheadpic);
    }

    public TeaBean() {
    }

    protected TeaBean(Parcel in) {
        this.teacherid = in.readString();
        this.teachername = in.readString();
        this.teacherheadpic = in.readString();
    }

    public static final Parcelable.Creator<TeaBean> CREATOR = new Parcelable.Creator<TeaBean>() {
        @Override
        public TeaBean createFromParcel(Parcel source) {
            return new TeaBean(source);
        }

        @Override
        public TeaBean[] newArray(int size) {
            return new TeaBean[size];
        }
    };
}
