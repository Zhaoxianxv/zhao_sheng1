package com.yfy.app.process.bean;

public class ProRecordList {

    /**
     * id : 13.0
     * HeadPic : /uploadfile/files/20200803170800726.jpg
     * content : 修改申请内容
     * stateid : 2
     * state : 申请请假
     * adddate : 2020/07/17
     * realname : 管理员
     * images :
     * voice :
     */

    private double id;
    private String HeadPic;
    private String content;
    private String stateid;
    private String state;
    private String adddate;
    private String realname;
    private String images;
    private String voice;
    private String statecolor;


    public String getStatecolor() {
        return statecolor;
    }

    public void setStatecolor(String statecolor) {
        this.statecolor = statecolor;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
