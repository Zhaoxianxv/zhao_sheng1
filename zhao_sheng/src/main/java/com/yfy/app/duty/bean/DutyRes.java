package com.yfy.app.duty.bean;

import java.util.List;

public class DutyRes {
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

    /**
     * -----------------------plane---------------
     */
    private List<PlaneInfo> dutyreport_plane;

    public List<PlaneInfo> getDutyreport_plane() {
        return dutyreport_plane;
    }

    public void setDutyreport_plane(List<PlaneInfo> dutyreport_plane) {
        this.dutyreport_plane = dutyreport_plane;
    }
    /**
     * -----------add-------------
     */
    private List<Addinfo> dutyreport_type;

    public List<Addinfo> getDutyreport_type() {
        return dutyreport_type;
    }

    public void setDutyreport_type(List<Addinfo> dutyreport_type) {
        this.dutyreport_type = dutyreport_type;
    }

    /**
     * --------------------main-----------------
     */
    private List<Info> dutyreport_list;

    public List<Info> getDutyreport_list() {
        return dutyreport_list;
    }

    public void setDutyreport_list(List<Info> dutyreport_list) {
        this.dutyreport_list = dutyreport_list;
    }
    /**
     * ------------------week--------------
     */
    private List<WeekInfo> terms;

    public List<WeekInfo> getTerms() {
        return terms;
    }

    public void setTerms(List<WeekInfo> terms) {
        this.terms = terms;
    }
}
