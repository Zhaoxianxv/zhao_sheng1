package com.yfy.app.duty.bean;

import java.util.List;

public class Addinfo {

    /**
     * typeid : 0
     * typename : 校级行政值周
     */

    private String typeid;
    private String typename;
    private List<AddBean> dutyreport_content;

    public List<AddBean> getDutyreport_content() {
        return dutyreport_content;
    }

    public void setDutyreport_content(List<AddBean> dutyreport_content) {
        this.dutyreport_content = dutyreport_content;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
