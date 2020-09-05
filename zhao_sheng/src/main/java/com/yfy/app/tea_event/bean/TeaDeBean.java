package com.yfy.app.tea_event.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

public class TeaDeBean implements Parcelable {



    private int view_type=TagFinal.TYPE_CHILD;
    /**
     * selectid : 5
     * selecttitle : 体罚或变相体罚学生，歧视、侮辱学生，或有其他侵犯学生合法权益行为，造成严重后果；
     * selectischeck : false
     *                  "maxcount": "2",
     *                   "checkcount": "0",
     *                   "percentage": "0.0%"
     */

    private String selectid;
    private String selecttitle;
    private String selectischeck;
    private String maxcount;
    private String checkcount;
    private String percentage;
    private String isShow=TagFinal.FALSE;


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

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getSelectid() {
        return selectid;
    }

    public void setSelectid(String selectid) {
        this.selectid = selectid;
    }

    public String getSelecttitle() {
        return selecttitle;
    }

    public void setSelecttitle(String selecttitle) {
        this.selecttitle = selecttitle;
    }

    public String getSelectischeck() {
        return selectischeck;
    }

    public void setSelectischeck(String selectischeck) {
        this.selectischeck = selectischeck;
    }

    /**
     * id : 1
     * title : 很满意
     * ischeck : false
     * word :
     * evaluatelast : []
     */

    private String id;
    private String title;
    private String ischeck=TagFinal.FALSE;
    private String word;

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
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
        dest.writeInt(this.view_type);
        dest.writeString(this.selectid);
        dest.writeString(this.selecttitle);
        dest.writeString(this.selectischeck);
        dest.writeString(this.maxcount);
        dest.writeString(this.checkcount);
        dest.writeString(this.percentage);
        dest.writeString(this.isShow);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.ischeck);
        dest.writeString(this.word);
        dest.writeString(this.evaluateid);
        dest.writeString(this.evaluatetitle);
        dest.writeString(this.type);
        dest.writeString(this.maxword);
        dest.writeString(this.content);
    }

    public TeaDeBean() {
    }

    protected TeaDeBean(Parcel in) {
        this.view_type = in.readInt();
        this.selectid = in.readString();
        this.selecttitle = in.readString();
        this.selectischeck = in.readString();
        this.maxcount = in.readString();
        this.checkcount = in.readString();
        this.percentage = in.readString();
        this.isShow = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.ischeck = in.readString();
        this.word = in.readString();
        this.evaluateid = in.readString();
        this.evaluatetitle = in.readString();
        this.type = in.readString();
        this.maxword = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<TeaDeBean> CREATOR = new Parcelable.Creator<TeaDeBean>() {
        @Override
        public TeaDeBean createFromParcel(Parcel source) {
            return new TeaDeBean(source);
        }

        @Override
        public TeaDeBean[] newArray(int size) {
            return new TeaDeBean[size];
        }
    };
}
