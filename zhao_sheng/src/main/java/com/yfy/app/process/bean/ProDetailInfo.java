package com.yfy.app.process.bean;

import java.util.List;

public class ProDetailInfo {

    /**
     * id : 5.0
     * userid : 1
     * HeadPic : /uploadfile/files/20200803170800726.jpg
     * group : none
     * secondgroup : none
     * title : 猜猜猜
     * stateid : 2
     * detailtype : attendance
     * state : 申请请假
     * adddate : 2020/07/17
     * username : 管理员
     */

    private double id;
    private int userid;
    private String HeadPic;
    private String group;
    private String secondgroup;
    private String title;
    private String stateid;
    private String detailtype;
    private String state;
    private String adddate;
    private String username;

    private List<ProRecordList> records;
    private List<ProLayerOneData> recordvalue;
    private List<ProOperation> recordoperation;


    public List<ProRecordList> getRecords() {
        return records;
    }

    public void setRecords(List<ProRecordList> records) {
        this.records = records;
    }

    public List<ProLayerOneData> getRecordvalue() {
        return recordvalue;
    }

    public void setRecordvalue(List<ProLayerOneData> recordvalue) {
        this.recordvalue = recordvalue;
    }

    public List<ProOperation> getRecordoperation() {
        return recordoperation;
    }

    public void setRecordoperation(List<ProOperation> recordoperation) {
        this.recordoperation = recordoperation;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSecondgroup() {
        return secondgroup;
    }

    public void setSecondgroup(String secondgroup) {
        this.secondgroup = secondgroup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getDetailtype() {
        return detailtype;
    }

    public void setDetailtype(String detailtype) {
        this.detailtype = detailtype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
