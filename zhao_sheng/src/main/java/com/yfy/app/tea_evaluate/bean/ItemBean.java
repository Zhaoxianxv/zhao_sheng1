package com.yfy.app.tea_evaluate.bean;

public class ItemBean {

    /**
     * title : 2015/12/30 2:27:28---管理员
     * id : 1
     * date : 2015/12/30
     * issubmit : 是
     * score : 0.80
     * {
     * 	"title": "2019/1/7 17:00:24---管理员",
     * 	"id": "145836",
     * 	"date": "2019/1/7",
     * 	"issubmit": "是",
     * 	"state": "未通过",
     * 	"score": "0.00",
     * 	"reason": "测试"
     * }
     */

    private String title;
    private String id;
    private String date;
    private String issubmit;
    private String state;
    private String reason;
    private String score;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssubmit() {
        return issubmit;
    }

    public void setIssubmit(String issubmit) {
        this.issubmit = issubmit;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
