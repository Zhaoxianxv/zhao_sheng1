package com.yfy.app.event.bean;

import com.yfy.app.event.bean.EventBean;

import java.util.List;

public class EventDep {

    /**
     * departmentid : 1
     * departmentname : 学校文化
     */

    private String departmentid;
    private String departmentname;
    private List<EventBean> evenet_list;

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public List<EventBean> getEvenet_list() {
        return evenet_list;
    }

    public void setEvenet_list(List<EventBean> evenet_list) {
        this.evenet_list = evenet_list;
    }
}
