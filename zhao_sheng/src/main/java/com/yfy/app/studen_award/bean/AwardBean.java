package com.yfy.app.studen_award.bean;

import java.util.List;

/**
 * Created by yfy1 on 2017/4/11.
 */

public class AwardBean {

    /**
     * score : 1.0
     * images :
     * teacher : 游佳
     * id : 10017
     * type : 作业情况
     * content :
     */
    private String date;
    private String type;
    private String content;
    private String score;
    private List<String> images;
    private String teacher;
    private String id;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
