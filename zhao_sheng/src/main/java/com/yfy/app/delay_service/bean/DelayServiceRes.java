package com.yfy.app.delay_service.bean;


import java.util.List;

public class DelayServiceRes {


    private String result;
    private String error_code;
    private String canedit;
    //---------------grade-------------
    private List<EventGrade> Elective_class;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }


    public List<EventGrade> getElective_class() {
        return Elective_class;
    }

    public void setElective_class(List<EventGrade> elective_class) {
        Elective_class = elective_class;
    }

    public String getCanedit() {
        return canedit;
    }

    public void setCanedit(String canedit) {
        this.canedit = canedit;
    }


    //-------------------------alter list-------------
    private List<AbsentInfo> Elective_classdetail;

    public List<AbsentInfo> getElective_classdetail() {
        return Elective_classdetail;
    }

    public void setElective_classdetail(List<AbsentInfo> elective_classdetail) {
        Elective_classdetail = elective_classdetail;
    }


    //----------------Elective_list-----------

    private List<DelayAbsenteeismClass> Elective_list;

    public List<DelayAbsenteeismClass> getElective_list() {
        return Elective_list;
    }

    public void setElective_list(List<DelayAbsenteeismClass> elective_list) {
        Elective_list = elective_list;
    }

    //----------------Elective_admin----------------
    private List<DelayEventBean> Elective_admin;

    public List<DelayEventBean> getElective_admin() {
        return Elective_admin;
    }

    public void setElective_admin(List<DelayEventBean> elective_admin) {
        Elective_admin = elective_admin;
    }


    //-------------------
    private List<EventClass> Elective_stuetail;
    private List<EventClass> Elective_detail;

    public List<EventClass> getElective_detail() {
        return Elective_detail;
    }

    public void setElective_detail(List<EventClass> elective_detail) {
        Elective_detail = elective_detail;
    }

    public List<EventClass> getElective_stuetail() {
        return Elective_stuetail;
    }

    public void setElective_stuetail(List<EventClass> elective_stuetail) {
        Elective_stuetail = elective_stuetail;
    }
}
