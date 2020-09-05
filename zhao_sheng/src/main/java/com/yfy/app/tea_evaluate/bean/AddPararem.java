package com.yfy.app.tea_evaluate.bean;

import java.util.List;

public class AddPararem {

    /**
     * name : 荣誉称号
     * id : 10
     * type : text
     * info : []
     */

    private String name;
    private String title;
    private String id;
    private String type;
    private List<GradeBean> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GradeBean> getInfo() {
        return info;
    }

    public void setInfo(List<GradeBean> info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
