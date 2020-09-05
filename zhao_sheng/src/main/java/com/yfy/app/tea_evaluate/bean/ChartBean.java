package com.yfy.app.tea_evaluate.bean;

public class ChartBean {

    /**
     * title : 课例类
     * score : 0
     * middle_score : 0//中位数
     * max_score : 0
     * comments :
     * id : 2
     */

    private String title;
    private String score;
    private String middle_score;
    private String max_score;
    private String comments;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMiddle_score() {
        return middle_score;
    }

    public void setMiddle_score(String middle_score) {
        this.middle_score = middle_score;
    }

    public String getMax_score() {
        return max_score;
    }

    public void setMax_score(String max_score) {
        this.max_score = max_score;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
