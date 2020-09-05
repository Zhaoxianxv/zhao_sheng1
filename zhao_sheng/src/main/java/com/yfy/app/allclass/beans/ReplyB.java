package com.yfy.app.allclass.beans;

import java.io.Serializable;

/**
 * Created by yfy1 on 2017/3/21.
 */

public class ReplyB implements Serializable {

    /**
     * auth : 张红梅
     * authid : 132
     * headPic : http://125.70.244.11//uploadfile/photo/20170320164953.jpg
     * name : yfy
     * reply_content : Hh
     * replyid : 105753
     * student_id : 0
     * tearcherid : 535
     * time : 2017/3/21 12:09:38
     */

    private String auth;
    private String authid;
    private String headPic;
    private String name;
    private String reply_content;
    private String replyid;
    private String student_id;
    private String tearcherid;
    private String time;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getTearcherid() {
        return tearcherid;
    }

    public void setTearcherid(String tearcherid) {
        this.tearcherid = tearcherid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
