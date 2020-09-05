package com.yfy.app.ebook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yfyandr on 2017/8/31.
 */

public class EbookBean implements Parcelable {

    /**
     * image : http://www.cdeps.sc.cn/uploadfile/1.png
     * user_avatar : http://www.cdeps.sc.cn//uploadfile/photo/20170616115129.jpg
     * user_name : 管理员
     * tag_name : 测试2
     * fileurl : http://www.cdeps.sc.cn/1.txt
     * praise_count : 0
     * is_praise : false
     * id : 3
     * time : 2017/8/8 0:00:00
     * title : 学习分享
     * reply_count : 0
     * content : 123
     */

    private String image;
    private String user_avatar;
    private String user_name;
    private String tag_name;
    private String fileurl;
    private String praise_count;
    private String is_praise;
    private String id;
    private String time;
    private String title;
    private String reply_count;
    private String content;
    private String filePath;
    private boolean isdown=false;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isdown() {
        return isdown;
    }

    public void setIsdown(boolean isdown) {
        this.isdown = isdown;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public String getFileurl() {
        return fileurl;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getReply_count() {
        return reply_count;
    }

    public String getContent() {
        return content;
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
        dest.writeString(this.tag_name);
        dest.writeString(this.fileurl);
        dest.writeString(this.praise_count);
        dest.writeString(this.is_praise);
        dest.writeString(this.id);
        dest.writeString(this.time);
        dest.writeString(this.title);
        dest.writeString(this.reply_count);
        dest.writeString(this.content);
        dest.writeString(this.filePath);
        dest.writeByte(this.isdown ? (byte) 1 : (byte) 0);
    }

    public EbookBean() {
    }

    protected EbookBean(Parcel in) {
        this.image = in.readString();
        this.user_avatar = in.readString();
        this.user_name = in.readString();
        this.tag_name = in.readString();
        this.fileurl = in.readString();
        this.praise_count = in.readString();
        this.is_praise = in.readString();
        this.id = in.readString();
        this.time = in.readString();
        this.title = in.readString();
        this.reply_count = in.readString();
        this.content = in.readString();
        this.filePath = in.readString();
        this.isdown = in.readByte() != 0;
    }

    public static final Creator<EbookBean> CREATOR = new Creator<EbookBean>() {
        @Override
        public EbookBean createFromParcel(Parcel source) {
            return new EbookBean(source);
        }

        @Override
        public EbookBean[] newArray(int size) {
            return new EbookBean[size];
        }
    };
}
