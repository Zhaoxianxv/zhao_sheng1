package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.PriorityQueue;

public class TeaDe implements Parcelable {

    /**
     * id : 1
     * title : 很满意
     * ischeck : false
     * word :
     * evaluatelast : []
     */

    private String id;
    private String title;
    private String ischeck;
    private String word;
    private String maxcount;
    private String checkcount;
    private String percentage;
    private List<TeaDeBean> evaluatelast;


    public String getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(String maxcount) {
        this.maxcount = maxcount;
    }

    public String getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(String checkcount) {
        this.checkcount = checkcount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }



    public List<TeaDeBean> getEvaluatelast() {
        return evaluatelast;
    }

    public void setEvaluatelast(List<TeaDeBean> evaluatelast) {
        this.evaluatelast = evaluatelast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.ischeck);
        dest.writeString(this.word);
        dest.writeString(this.maxcount);
        dest.writeString(this.checkcount);
        dest.writeString(this.percentage);
        dest.writeTypedList(this.evaluatelast);
    }

    public TeaDe() {
    }

    protected TeaDe(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.ischeck = in.readString();
        this.word = in.readString();
        this.maxcount = in.readString();
        this.checkcount = in.readString();
        this.percentage = in.readString();
        this.evaluatelast = in.createTypedArrayList(TeaDeBean.CREATOR);
    }

    public static final Parcelable.Creator<TeaDe> CREATOR = new Parcelable.Creator<TeaDe>() {
        @Override
        public TeaDe createFromParcel(Parcel source) {
            return new TeaDe(source);
        }

        @Override
        public TeaDe[] newArray(int size) {
            return new TeaDe[size];
        }
    };
}
