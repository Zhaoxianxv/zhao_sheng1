package com.yfy.app.vote.bean;

import java.util.List;

/**
 * Created by yfy1 on 2017/3/25.
 */

public class VoteInfo {

    /**
     * Votecontent : [{"content":"演示了","id":"1100","isselect":"false"}]
     * id : 1100
     * maxsize : 1
     * minsize : 1
     * title : 问答
     * type : 3
     */

    private String id;
    private int maxsize;
    private int minsize;
    private String title;
    private String type;
    private String reply="";
    private List<VoteBean> Votecontent;


    public List<VoteBean> getVotecontent() {
        return Votecontent;
    }

    public void setVotecontent(List<VoteBean> votecontent) {
        Votecontent = votecontent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(int maxsize) {
        this.maxsize = maxsize;
    }

    public int getMinsize() {
        return minsize;
    }

    public void setMinsize(int minsize) {
        this.minsize = minsize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
