package com.yfy.app.integral.subjcet;

import com.yfy.app.integral.beans.Course;

import java.util.List;

/**
 * Created by yfyandr on 2018/1/23.
 */

public class ClassName {
    /**
     * class_id : 92
     * class_name : 一年级一班
     */
    private String classid;
    private String classname;
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

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
