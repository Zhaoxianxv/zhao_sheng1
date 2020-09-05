package com.yfy.app.vote.bean;

/**
 * Created by yfy1 on 2017/3/25.
 */

public class VoteBean {

    /**
     * content : 演示了
     * id : 1100
     * isselect : false
     */

    private String content;
    private String id;
    private String isselect;

    private boolean ischeckbox=false;

    public boolean ischeckbox() {
        return ischeckbox;
    }

    public void setIscheckbox(boolean ischeckbox) {
        this.ischeckbox = ischeckbox;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsselect() {
        return isselect;
    }

    public void setIsselect(String isselect) {
        this.isselect = isselect;
    }

    @Override
    public String toString() {
        return "VoteBean{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", isselect='" + isselect + '\'' +
                ", ischeckbox=" + ischeckbox +
                '}';
    }
}
