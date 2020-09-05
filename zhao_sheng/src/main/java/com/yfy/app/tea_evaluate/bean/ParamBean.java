package com.yfy.app.tea_evaluate.bean;

import java.util.List;

public class ParamBean {

    /**
     * title : 获奖级别
     * content : 1
     * id : 74
     * type : select
     * info : ["区级","市级","省级","国家级"]
     */

    private String title;
    private String content;
    private String id;
    private String type;
    private List<GradeBean> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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


    public List<GradeBean> getInfo() {
        return info;
    }

    public void setInfo(List<GradeBean> info) {
        this.info = info;
    }
}
