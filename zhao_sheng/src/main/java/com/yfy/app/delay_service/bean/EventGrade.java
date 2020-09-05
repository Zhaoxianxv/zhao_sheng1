package com.yfy.app.delay_service.bean;


import java.util.List;

public class EventGrade {

    /**
     * gradename : 1年级
     * gradeid : 0
     */

    private String gradename;
    private String gradeid;
    private List<AbsentInfo> Elective_list;

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }


    public List<AbsentInfo> getElective_list() {
        return Elective_list;
    }

    public void setElective_list(List<AbsentInfo> elective_list) {
        Elective_list = elective_list;
    }
}
