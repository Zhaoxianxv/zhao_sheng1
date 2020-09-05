package com.yfy.app.tea_evaluate.bean;

import java.util.List;

public class ChartInfo {
    private String year;
    private String comments;
    private List<ChartBean> info;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<ChartBean> getInfo() {
        return info;
    }

    public void setInfo(List<ChartBean> info) {
        this.info = info;
    }
}
