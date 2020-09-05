package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TeaEventRes implements Parcelable {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    /**
     * ----------------teacher----------------
     */
    private List<Teacher> teachers;

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    /**
     * -----------------------info---------------------
     */
    private List<TeaDeInfo> evaluatelist;

    public List<TeaDeInfo> getEvaluatelist() {
        return evaluatelist;
    }

    public void setEvaluatelist(List<TeaDeInfo> evaluatelist) {
        this.evaluatelist = evaluatelist;
    }


    private List<Stu> incompletestu;

    public List<Stu> getIncompletestu() {
        return incompletestu;
    }

    public void setIncompletestu(List<Stu> incompletestu) {
        this.incompletestu = incompletestu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.result);
        dest.writeString(this.error_code);
        dest.writeTypedList(this.teachers);
        dest.writeTypedList(this.evaluatelist);
        dest.writeTypedList(this.incompletestu);
    }

    public TeaEventRes() {
    }

    protected TeaEventRes(Parcel in) {
        this.result = in.readString();
        this.error_code = in.readString();
        this.teachers = in.createTypedArrayList(Teacher.CREATOR);
        this.evaluatelist = in.createTypedArrayList(TeaDeInfo.CREATOR);
        this.incompletestu = in.createTypedArrayList(Stu.CREATOR);
    }

    public static final Parcelable.Creator<TeaEventRes> CREATOR = new Parcelable.Creator<TeaEventRes>() {
        @Override
        public TeaEventRes createFromParcel(Parcel source) {
            return new TeaEventRes(source);
        }

        @Override
        public TeaEventRes[] newArray(int size) {
            return new TeaEventRes[size];
        }
    };
}
