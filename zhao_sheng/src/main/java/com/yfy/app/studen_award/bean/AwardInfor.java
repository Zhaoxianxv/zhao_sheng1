package com.yfy.app.studen_award.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2017/4/6.
 */

public class AwardInfor {
    private List<GradeBean> classes;
    private List<AwardType> types;
    private ArrayList<AwardStuBean> students;



    private String size;



    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public List<AwardStuBean> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<AwardStuBean> students) {
        this.students = students;
    }

    public List<GradeBean> getClasses() {
        return classes;
    }

    public void setClasses(List<GradeBean> classes) {
        this.classes = classes;
    }

    public List<AwardType> getTypes() {
        return types;
    }

    public void setTypes(List<AwardType> types) {
        this.types = types;
    }

    /**
     * ----------------------award stu list------------
     */
    private List<AwardInfo> terms;
    public List<AwardInfo> getTerms() {
        return terms;
    }
    public void setTerms(List<AwardInfo> terms) {
        this.terms = terms;
    }
}
