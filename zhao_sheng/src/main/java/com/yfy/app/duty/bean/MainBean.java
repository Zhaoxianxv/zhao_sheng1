package com.yfy.app.duty.bean;

import java.util.List;

public class MainBean {


    /**
     * title : 上学时段校园情况是否正常
     * titleid : 1
     * staffid : 1
     * realname : 管理员
     * content :
     * isnormal : true
     * image : ["https://www.cdeps.sc.cn/uploadfile/dutyreport/1//storage/emulated/0/yfy/1552014073769.jpg","https://www.cdeps.sc.cn/uploadfile/dutyreport/1//storage/emulated/0/yfy/1552012381383.jpg"]
     */

    private String title;
    private String titleid;
    private String staffid;
    private String realname;
    private String content;
    private String isnormal;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleid() {
        return titleid;
    }

    public void setTitleid(String titleid) {
        this.titleid = titleid;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsnormal() {
        return isnormal;
    }

    public void setIsnormal(String isnormal) {
        this.isnormal = isnormal;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
