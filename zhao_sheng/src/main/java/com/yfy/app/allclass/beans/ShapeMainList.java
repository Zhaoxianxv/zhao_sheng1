package com.yfy.app.allclass.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfyandr on 2017/5/9.
 */

public class ShapeMainList implements Parcelable{

    /**
     * images : ["http://www.cdeps.sc.cn/uploadfile/wxq/228/201705091412460550.jpg"]
     * name : 游佳
     * isPraise : 否
     * praisecount : 1
     * time : 2017/05/09 14:12
     * id : 1
     * reply_count : 3
     * head_photo : http://www.cdeps.sc.cn/head.png
     * praise : 游佳
     * content : 时光荏苒，沧海桑田。当悠远的驼铃声穿越
     * tags : 人文
     */

    private List<String> images;
    private String name;
    private String isPraise;
    private String praisecount;
    private String time;
    private String id;
    private String reply_count;
    private String head_photo;
    private String praise;
    private String content;
    private String tags;
    private String user_id;
    private String user_type;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public void setPraisecount(String praisecount) {
        this.praisecount = praisecount;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public void setHead_photo(String head_photo) {
        this.head_photo = head_photo;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getName() {
        return name;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public String getPraisecount() {
        return praisecount;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public String getReply_count() {
        return reply_count;
    }

    public String getHead_photo() {
        return head_photo;
    }

    public String getPraise() {
        return praise;
    }

    public String getContent() {
        return content;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.images);
        dest.writeString(this.name);
        dest.writeString(this.isPraise);
        dest.writeString(this.praisecount);
        dest.writeString(this.time);
        dest.writeString(this.id);
        dest.writeString(this.reply_count);
        dest.writeString(this.head_photo);
        dest.writeString(this.praise);
        dest.writeString(this.content);
        dest.writeString(this.tags);
        dest.writeString(this.user_id);
        dest.writeString(this.user_type);
    }

    public ShapeMainList() {
    }

    protected ShapeMainList(Parcel in) {
        this.images = in.createStringArrayList();
        this.name = in.readString();
        this.isPraise = in.readString();
        this.praisecount = in.readString();
        this.time = in.readString();
        this.id = in.readString();
        this.reply_count = in.readString();
        this.head_photo = in.readString();
        this.praise = in.readString();
        this.content = in.readString();
        this.tags = in.readString();
        this.user_id = in.readString();
        this.user_type = in.readString();
    }

    public static final Creator<ShapeMainList> CREATOR = new Creator<ShapeMainList>() {
        @Override
        public ShapeMainList createFromParcel(Parcel source) {
            return new ShapeMainList(source);
        }

        @Override
        public ShapeMainList[] newArray(int size) {
            return new ShapeMainList[size];
        }
    };
}
