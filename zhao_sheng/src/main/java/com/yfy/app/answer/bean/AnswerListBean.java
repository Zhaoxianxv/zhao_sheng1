package com.yfy.app.answer.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.studen_award.bean.AwardStuBean;

/**
 * Created by yfyandr on 2017/8/2.
 */

public class AnswerListBean implements Parcelable {

    /**
     * image : http://www.cdeps.sc.cn//uploadfile/question/tea119/20170724144758.jpg
     * user_avatar :
     * user_name : 黄天舟
     * canaccept : false
     * isanswer : true
     * id : 3
     * time : 2017/7/24 14:47:58
     * answer_count : 2
     * content : 演示文稿
     */


    private String image;
    private String user_avatar;
    private String user_name;
    private String canaccept;
    private String isanswer;
    private String id;
    private String time;
    private String answer_count;
    private String content;
    private String is_praise;
    private String praise_count;
    private String candel;


    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getCandel() {
        return candel;
    }

    public void setCandel(String candel) {
        this.candel = candel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCanaccept() {
        return canaccept;
    }

    public void setCanaccept(String canaccept) {
        this.canaccept = canaccept;
    }

    public String getIsanswer() {
        return isanswer;
    }

    public void setIsanswer(String isanswer) {
        this.isanswer = isanswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(String answer_count) {
        this.answer_count = answer_count;
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
        dest.writeString(this.image);
        dest.writeString(this.user_avatar);
        dest.writeString(this.user_name);
        dest.writeString(this.canaccept);
        dest.writeString(this.isanswer);
        dest.writeString(this.id);
        dest.writeString(this.time);
        dest.writeString(this.answer_count);
        dest.writeString(this.content);
    }

    public AnswerListBean() {
    }

    protected AnswerListBean(Parcel in) {
        this.image = in.readString();
        this.user_avatar = in.readString();
        this.user_name = in.readString();
        this.canaccept = in.readString();
        this.isanswer = in.readString();
        this.id = in.readString();
        this.time = in.readString();
        this.answer_count = in.readString();
        this.content = in.readString();
    }

    public static final Creator<AnswerListBean> CREATOR = new Creator<AnswerListBean>() {
        @Override
        public AnswerListBean createFromParcel(Parcel source) {
            return new AnswerListBean(source);
        }

        @Override
        public AnswerListBean[] newArray(int size) {
            return new AnswerListBean[size];
        }
    };
}
