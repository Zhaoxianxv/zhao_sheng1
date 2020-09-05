package com.yfy.app.integral.subjcet;

/**
 * Created by yfyandr on 2018/1/23.
 */

public class StuAwrad {

    /**
     * classid : 92
     * classname : 二年级一班
     * coursename : 语文
     * stuid : 2157
     * isaward : false
     * courseid : 1
     * stuname : 刘子龙
     */
    private String classid;
    private String classname;
    private String coursename;
    private String stuid;
    private String isaward;
    private String courseid;
    private String stuname;
    private boolean selecter=false;

    public boolean isSelecter() {
        return selecter;
    }

    public void setSelecter(boolean selecter) {
        this.selecter = selecter;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public void setIsaward(String isaward) {
        this.isaward = isaward;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getClassid() {
        return classid;
    }

    public String getClassname() {
        return classname;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getStuid() {
        return stuid;
    }

    public String getIsaward() {
        return isaward;
    }

    public String getCourseid() {
        return courseid;
    }

    public String getStuname() {
        return stuname;
    }
}
