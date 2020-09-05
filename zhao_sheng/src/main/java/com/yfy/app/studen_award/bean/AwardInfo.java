package com.yfy.app.studen_award.bean;

import java.util.List;
import java.util.PriorityQueue;

public class AwardInfo {
//    {
//        "termid": "3",
//            "termname": "2017-2018上期",
//            "count": "6",
//            "score": "6",
//            "laughscore": "6",
//            "laughcount": "6",
//            "cryscore": "0",
//            "crycount": "0",

    private String termid;
    private String termname;
    private String count;
    private String score;
    private String laughscore;
    private String laughcount;
    private String cryscore;
    private String crycount;
    private List<AwardBean> awardinfo;


    public String getLaughcount() {
        return laughcount;
    }

    public void setLaughcount(String laughcount) {
        this.laughcount = laughcount;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public List<AwardBean> getAwardinfo() {
        return awardinfo;
    }

    public void setAwardinfo(List<AwardBean> awardinfo) {
        this.awardinfo = awardinfo;
    }
}
