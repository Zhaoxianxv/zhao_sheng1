package com.yfy.app.attennew.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yfyandr on 2018/1/4.
 */

public class AttenBean implements Parcelable {


    /**
     * duration : 1.0天
     * user_avatar : http://www.cdeps.sc.cn//uploadfile/photo/20171222112306.jpg
     * reason : 123
     * dealstate : 申请请假
     * submit_time : 2018/01/03 10:57
     * user_name : 管理员
     * table_plan :
     * Approvername : 管理员
     * id : 4030
     * type : 病假
     * reply :
     * adddate : 2018/01/03 00:00
     */
    private String duration;
    private String user_avatar;
    private String reason;
    private String dealstate;
    private String submit_time;
    private String user_name;
    private String table_plan;
    private String Approvername;
    private String id;
    private String type;
    private String reply;
    private String adddate;
    private List<String> image;
    private List<AttenFlow> attendance_info;

    public List<AttenFlow> getAttendance_info() {
        return attendance_info;
    }

    public void setAttendance_info(List<AttenFlow> attendance_info) {
        this.attendance_info = attendance_info;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDealstate(String dealstate) {
        this.dealstate = dealstate;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTable_plan(String table_plan) {
        this.table_plan = table_plan;
    }

    public void setApprovername(String Approvername) {
        this.Approvername = Approvername;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getDuration() {
        return duration;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public String getReason() {
        return reason;
    }

    public String getDealstate() {
        return dealstate;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTable_plan() {
        return table_plan;
    }

    public String getApprovername() {
        return Approvername;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getReply() {
        return reply;
    }

    public String getAdddate() {
        return adddate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.duration);
        dest.writeString(this.user_avatar);
        dest.writeString(this.reason);
        dest.writeString(this.dealstate);
        dest.writeString(this.submit_time);
        dest.writeString(this.user_name);
        dest.writeString(this.table_plan);
        dest.writeString(this.Approvername);
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.reply);
        dest.writeString(this.adddate);
        dest.writeStringList(this.image);
        dest.writeTypedList(this.attendance_info);
    }

    public AttenBean() {
    }

    protected AttenBean(Parcel in) {
        this.duration = in.readString();
        this.user_avatar = in.readString();
        this.reason = in.readString();
        this.dealstate = in.readString();
        this.submit_time = in.readString();
        this.user_name = in.readString();
        this.table_plan = in.readString();
        this.Approvername = in.readString();
        this.id = in.readString();
        this.type = in.readString();
        this.reply = in.readString();
        this.adddate = in.readString();
        this.image = in.createStringArrayList();
        this.attendance_info = in.createTypedArrayList(AttenFlow.CREATOR);
    }

    public static final Parcelable.Creator<AttenBean> CREATOR = new Parcelable.Creator<AttenBean>() {
        @Override
        public AttenBean createFromParcel(Parcel source) {
            return new AttenBean(source);
        }

        @Override
        public AttenBean[] newArray(int size) {
            return new AttenBean[size];
        }
    };
}
