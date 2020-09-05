package com.yfy.app.delay_service.bean;


import java.util.List;

public class EventRes {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;

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
    //----------------------class_choice-------------------
    private List<AbsentInfo> Elective_class;


    public List<AbsentInfo> getElective_class() {
        return Elective_class;
    }

    public void setElective_class(List<AbsentInfo> elective_class) {
        Elective_class = elective_class;
    }
    private String situation_count;
    private String Unattendance_count;
    private String absent_count;
    private String arrive_count;

    public String getArrive_count() {
        return arrive_count;
    }

    public void setArrive_count(String arrive_count) {
        this.arrive_count = arrive_count;
    }

    public String getUnattendance_count() {
        return Unattendance_count;
    }

    public void setUnattendance_count(String unattendance_count) {
        Unattendance_count = unattendance_count;
    }

    public String getAbsent_count() {

        return absent_count;
    }

    public void setAbsent_count(String absent_count) {
        this.absent_count = absent_count;
    }

    public String getSituation_count() {
        return situation_count;
    }

    public void setSituation_count(String situation_count) {
        this.situation_count = situation_count;
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
    //--------------------
    private List<AbsentInfo> Elective_list;

    public List<AbsentInfo> getElective_list() {
        return Elective_list;
    }

    public void setElective_list(List<AbsentInfo> elective_list) {
        Elective_list = elective_list;
    }



    //------------------opear---------
    private List<OperType> Elective_opear;

    public List<OperType> getElective_opear() {
        return Elective_opear;
    }

    public void setElective_opear(List<OperType> elective_opear) {
        Elective_opear = elective_opear;
    }



    private List<EventClass> Elective_classdetail;

    public List<EventClass> getElective_classdetail() {
        return Elective_classdetail;
    }

    public void setElective_classdetail(List<EventClass> elective_classdetail) {
        Elective_classdetail = elective_classdetail;
    }
}
