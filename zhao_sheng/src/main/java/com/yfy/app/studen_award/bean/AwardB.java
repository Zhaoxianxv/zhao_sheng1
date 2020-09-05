package com.yfy.app.studen_award.bean;

import com.yfy.final_tag.TagFinal;

import java.util.List;

public class AwardB {
    //type parent child
    private int type_view;

    //child
    private String date;
    private String type;
    private String content;
    private String score;
    private List<String> images;
    private String teacher;
    private String id;

    public AwardB(String date, String type, String content, String score, List<String> images, String teacher) {
        this.date = date;
        this.type = type;
        this.content = content;
        this.score = score;
        this.images = images;
        this.teacher = teacher;
        this.type_view = TagFinal.ONE_INT;
    }

    //parent
    private String termid;
    private String termname;
    private String count;
    private String laughscore;
    private String laughcount;
    private String cryscore;
    private String crycount;

    public AwardB(String termname, String count, String laughcount,  String crycount) {
        this.termname = termname;
        this.count = count;
        this.laughcount = laughcount;
        this.crycount = crycount;
        this.type_view = TagFinal.TWO_INT;
    }

    public int getType_view() {
        return type_view;
    }

    public void setType_view(int type_view) {
        this.type_view = type_view;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getLaughcount() {
        return laughcount;
    }

    public void setLaughcount(String laughcount) {
        this.laughcount = laughcount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTermid() {
        return termid;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLaughscore() {
        return laughscore;
    }

    public void setLaughscore(String laughscore) {
        this.laughscore = laughscore;
    }

    public String getCryscore() {
        return cryscore;
    }

    public void setCryscore(String cryscore) {
        this.cryscore = cryscore;
    }

    public String getCrycount() {
        return crycount;
    }

    public void setCrycount(String crycount) {
        this.crycount = crycount;
    }
}
