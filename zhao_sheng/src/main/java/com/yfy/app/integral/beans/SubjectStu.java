package com.yfy.app.integral.beans;

/**
 * Created by yfyandr on 2017/9/4.
 */

public class SubjectStu {
    /**
     * TermName : 2016-2017上期
     * coursename : 语文
     * teachername : 王勇
     */
    private String TermName;
    private String coursename;
    private String teachername;
    private String stuname;

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public void setTermName(String TermName) {
        this.TermName = TermName;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTermName() {
        return TermName;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getTeachername() {
        return teachername;
    }
}
