package com.yfy.app.duty.bean;

import java.util.List;

public class MainInfo {

    /**
     * type : 校级行政值周
     * typeid : 0
     */

    private String type;
    private String typeid;
    private String staffid;
    private String realname;
    private List<MainBean> dutyreport_list;

    public List<MainBean> getDutyreport_list() {
        return dutyreport_list;
    }

    public void setDutyreport_list(List<MainBean> dutyreport_list) {
        this.dutyreport_list = dutyreport_list;
    }


    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
