package com.yfy.app.allclass.beans;

/**
 * Created by yfyandr on 2018/3/12.
 */

public class TopBean {

    /**
     * classid : 0
     * post : 2
     * title : 总计
     * reply : 0
     */
    private String classid;
    private String post;
    private String title;
    private String reply;

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getClassid() {
        return classid;
    }

    public String getPost() {
        return post;
    }

    public String getTitle() {
        return title;
    }

    public String getReply() {
        return reply;
    }
}
