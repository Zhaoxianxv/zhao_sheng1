package com.yfy.app.duty.bean;

import com.yfy.final_tag.TagFinal;

import java.util.List;

public class MainB {

    private int viewType;
    //item
    private int child_bg;
    private String title;
    private String titleid;
    private String staffid;
    private String realname;
    private String content;
    private String isnormal;
    private String image;

    // 值周类型
    private String type;
    private String typeid;
    //time
    private String date;

    public MainB(String date) {
        this.date = date;
        viewType=TagFinal.ONE_INT;
    }

    public MainB(String realname, String type) {
        this.realname = realname;
        this.type = type;
        viewType=TagFinal.TWO_INT;
    }

    public MainB(String title, String content, String isnormal, String image) {
        this.title = title;
        this.content = content;
        this.isnormal = isnormal;
        this.image = image;
        viewType=TagFinal.THREE_INT;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getChild_bg() {
        return child_bg;
    }

    public void setChild_bg(int child_bg) {
        this.child_bg = child_bg;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
