package com.yfy.app.integral.beans;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/20.
 */

public class ClassName {

    /**
     * class_id : 92
     * class_name : 一年级一班
     */
    private String class_id;
    private String class_name;
    /**
     * courses
     */
    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getClass_name() {
        return class_name;
    }
}
