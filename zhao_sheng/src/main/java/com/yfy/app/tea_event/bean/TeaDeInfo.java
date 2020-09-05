package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TeaDeInfo implements Parcelable {
    /**
     * evaluateid : 1
     * evaluatetitle : 师德师风满意度
     * type : 单选
     * maxword : 0
     * content :
     */

    private String evaluateid;
    private String evaluatetitle;
    private String type;
    private String maxword;
    private String content;
    private List<String> words;

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    private List<TeaDe> evaluateselect;

    public List<TeaDe> getEvaluateselect() {
        return evaluateselect;
    }

    public void setEvaluateselect(List<TeaDe> evaluateselect) {
        this.evaluateselect = evaluateselect;
    }

    public String getEvaluateid() {
        return evaluateid;
    }

    public void setEvaluateid(String evaluateid) {
        this.evaluateid = evaluateid;
    }

    public String getEvaluatetitle() {
        return evaluatetitle;
    }

    public void setEvaluatetitle(String evaluatetitle) {
        this.evaluatetitle = evaluatetitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxword() {
        return maxword;
    }

    public void setMaxword(String maxword) {
        this.maxword = maxword;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.evaluateid);
        dest.writeString(this.evaluatetitle);
        dest.writeString(this.type);
        dest.writeString(this.maxword);
        dest.writeString(this.content);
        dest.writeStringList(this.words);
        dest.writeTypedList(this.evaluateselect);
    }

    public TeaDeInfo() {
    }

    protected TeaDeInfo(Parcel in) {
        this.evaluateid = in.readString();
        this.evaluatetitle = in.readString();
        this.type = in.readString();
        this.maxword = in.readString();
        this.content = in.readString();
        this.words = in.createStringArrayList();
        this.evaluateselect = in.createTypedArrayList(TeaDe.CREATOR);
    }

    public static final Parcelable.Creator<TeaDeInfo> CREATOR = new Parcelable.Creator<TeaDeInfo>() {
        @Override
        public TeaDeInfo createFromParcel(Parcel source) {
            return new TeaDeInfo(source);
        }

        @Override
        public TeaDeInfo[] newArray(int size) {
            return new TeaDeInfo[size];
        }
    };
}
